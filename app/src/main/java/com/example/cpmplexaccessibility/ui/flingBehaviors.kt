package com.example.cpmplexaccessibility.ui

import android.view.ViewConfiguration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * This FlingBehavior assumes, that first item of the list is some sort of header, which should be
 * either fully expanded or fully collapsed. To accomplish this, it automatically scrolls header
 * before or after [wrapped] fling performs, based on some conditions. Top anchor is always 0, but
 * bottom anchor can be adjusted with [additionalOffset] parameter.
 */
@Composable
fun ScrollableDefaults.autoScrollFlingBehavior(
    lazyListState: LazyListState,
    additionalOffset: Density.() -> Int,
    wrapped: FlingBehavior = velocityLimitingFlingBehaviour()
): FlingBehavior {
    val density = LocalDensity.current
    return remember(lazyListState, wrapped, density, additionalOffset) {
        AutoScrollFlingBehavior(
            lazyListState = lazyListState,
            wrapped = wrapped,
            additionalOffset = { with(density) { additionalOffset() } }
        )
    }
}

/**
 * This FlingBehavior limits fling velocity with `ViewConfiguration.scaledMaximumFlingVelocity` and
 * compresses scrollBy values for smoother flinging on slower devices.
 */
@Composable
fun ScrollableDefaults.velocityLimitingFlingBehaviour(): FlingBehavior {
    val context = LocalContext.current
    val original = flingBehavior()
    return remember(original, context) {
        val maxFlingVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                val limitingScrollScope = LimitingScrollScope(this)
                val coercedVelocity = initialVelocity.coerceIn(-maxFlingVelocity, maxFlingVelocity)
                return with(original) { limitingScrollScope.performFling(coercedVelocity) }
            }
        }
    }
}

/**
 * If one frame was skipped due to long computation on main thread, default fling animation will
 * try to scroll twice as much on next frame, requiring main thread to do even more computation,
 * making animation to try to scroll even more and so on, snowballing into huge lags. This class
 * does several things to prevent these lags
 */
private class LimitingScrollScope(private val scrollScope: ScrollScope) : ScrollScope {

    // See also: https://issuetracker.google.com/issues/202423452

    private var lastMax = Float.POSITIVE_INFINITY
    private var skipFirst = true

    override fun scrollBy(pixels: Float): Float {
        val threshold = 50f
        val adjustedPixels = if (pixels in -threshold..threshold) {
            pixels
        } else {
            // compress high values after some threshold
            // https://www.desmos.com/calculator/makhr4dpo8
            val compressed = sqrt(abs(pixels) * threshold) * pixels.sign

            // prevent subsequent deltas from being bigger than previous
            minOf(abs(compressed), lastMax).also { lastMax = it } * compressed.sign
        }
        if (skipFirst) {
            skipFirst = false
            lastMax = Float.POSITIVE_INFINITY
        }
        val consumed = scrollScope.scrollBy(adjustedPixels)
        return if (abs(adjustedPixels - consumed) > 0.5f) consumed else pixels
    }
}

private class AutoScrollFlingBehavior(
    private val lazyListState: LazyListState,
    private val wrapped: FlingBehavior,
    private val additionalOffset: () -> Int,
) : FlingBehavior {

    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        val initialItemIndex = lazyListState.firstVisibleItemIndex

        // perform autoscroll to bottom anchor if fling performed towards bottom while list shows header
        if (initialItemIndex == 0 && initialVelocity > 200f) {
            val firstItemLayoutInfo = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull() ?: return initialVelocity
            val offset = -firstItemLayoutInfo.offset.toFloat()
            val size = firstItemLayoutInfo.size.toFloat()
            val additionalOffset = additionalOffset().coerceAtLeast(0)
            val targetValue = size - additionalOffset
            if (targetValue > offset) {
                var prevValue = offset
                val animatable = Animatable(offset)
                animatable.updateBounds(upperBound = targetValue)
                animatable.animateTo(targetValue, spring(stiffness = 400f), initialVelocity.coerceAtMost(1000f)) {
                    scrollBy(this.value - prevValue)
                    prevValue = this.value
                }
                return 0f
            }
        }

        // perform default fling
        val remainingVelocity = with(wrapped) { performFling(initialVelocity) }
        if (remainingVelocity.absoluteValue > 1f) return remainingVelocity

        // if stopped on header after fling is finished, perform autoscroll
        if (lazyListState.firstVisibleItemIndex == 0) {
            val firstItemLayoutInfo = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull() ?: return remainingVelocity
            val offset = -firstItemLayoutInfo.offset.toFloat()
            val size = firstItemLayoutInfo.size.toFloat()

            val additionalOffset = additionalOffset().coerceAtLeast(0)
            val shouldScrollToBottom = if (initialItemIndex > 1 || initialVelocity.absoluteValue < 200f) {
                // if header wasn't visible when fling started or fling was very weak - scroll to closest edge
                offset > (size - additionalOffset) / 2
            } else {
                // otherwise scroll in the direction of fling
                initialVelocity >= 0f
            }
            val targetValue = if (shouldScrollToBottom) {
                size - additionalOffset
            } else {
                0f
            }

            // If we're already scrolled past additionalOffset(), do not auto scroll back
            if (shouldScrollToBottom && targetValue <= offset) return 0f

            var prevValue = offset
            Animatable(offset).animateTo(targetValue, spring(stiffness = 500f)) {
                scrollBy(this.value - prevValue)
                prevValue = this.value
            }
            return 0f
        }
        return remainingVelocity
    }
}
