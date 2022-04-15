package com.example.cpmplexaccessibility.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

val TopBarHeight = 80.dp

class HeaderScreenScrollState(private val lazyListState: LazyListState) {

    val scrollPx: Int get() = lazyListState.firstItemOffset()
    val scrollFraction: Float get() = lazyListState.firstItemVisibilityFraction()
    val firstItemBottomY: Int get() = lazyListState.firstItemBottomY()

    val secondaryButtonsAlpha get() = lerp(scrollFraction, from = 0f..0.5f, to = 1f..0f).coerceIn(0f, 1f)

    val titleAlpha get() = lerp(scrollFraction, from = 0f..0.5f, to = 1f..0f).coerceIn(0f, 1f)
    val backgroundOverlayAlpha
        get() = lerp(scrollFraction, from = 0f..0.7f, to = 0f..1f).coerceIn(
            0f,
            1f
        )
}

@Composable
fun RevealingTopBar(
    scrollState: HeaderScreenScrollState,
    title: String,
    onBackClick: () -> Unit,
    onOverflowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // in landscape header may be short, and backgroundOverlayAlpha could still be low even
    // after flingBehavior stops, so we also check for distance to the top of the screen, and
    // force show top bar if we're close to the edge
    val triggerOffsetPx = with(LocalDensity.current) {
        (TopBarHeight + 8.dp).roundToPx()
    }
    val forceTopBarVisible = scrollState.firstItemBottomY <= triggerOffsetPx

    // Title will be visible slightly before background overlay fills 100%
    val isTitleVisible = scrollState.backgroundOverlayAlpha >= 0.8f

    // Top bar background must not be visible on top of header, so we show it when overlay is 100%
    val bgColorVisible = scrollState.backgroundOverlayAlpha == 1f

    RevealingTopBar(
        isTitleVisible = forceTopBarVisible || isTitleVisible,
        isBackgroundVisible = forceTopBarVisible || bgColorVisible,
        title = title,
        onBackClick = onBackClick,
        onOverflowClick = onOverflowClick,
        modifier = modifier,
    )
}

@Composable
private fun RevealingTopBar(
    isTitleVisible: Boolean,
    isBackgroundVisible: Boolean,
    title: String,
    onBackClick: () -> Unit,
    onOverflowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleAlpha by animateFloatAsState(targetValue = if (isTitleVisible) 1f else 0f)
    val bgColor = if (isBackgroundVisible) HeaderScreenColors.topBarColor else Color.Transparent

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .then(if (isBackgroundVisible) Modifier.pointerInput(Unit) { } else Modifier),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(TopBarHeight)
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .alpha(titleAlpha)
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = (48 + 4).dp, end = (48 + 4).dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // Header is always in dark theme, so even if current theme is light we want icons to
            // be in dark theme while they are above header

            val animatedColor by animateColorAsState(MaterialTheme.colors.onSurface)
            CompositionLocalProvider(LocalContentColor provides animatedColor) {
                TopBarButtons(
                    onBackClick = onBackClick,
                    onOverflowClick = onOverflowClick,
                )
            }
        }
    }
}