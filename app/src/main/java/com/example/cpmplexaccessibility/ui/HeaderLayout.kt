package com.example.cpmplexaccessibility.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

/**
 * Lays out header components using following logic:
 * * applies [topPadding] + additional padding to the top, and 16dp padding between [cover] and [title]
 * * places [cover] above [title], but only if its size would be more than 96dp
 * * places [title] and [description] in the middle
 * * places [topCover] above [cover], but below other elements
 * * leaving space for buttons at the bottom
 * * centers all components horizontally
 */
@Composable
fun HeaderLayout(
    modifier: Modifier = Modifier,
    cover: @Composable (() -> Unit)? = null,
    title: @Composable HeaderTitleScope.() -> Unit,
    description: @Composable (() -> Unit)? = null,
    topCover: @Composable (() -> Unit)? = null,
    buttons: @Composable (() -> Unit)? = null,
    coverOffset: () -> Int = { 0 },
    isCoverBranded: Boolean = false,
) {
    // There is a bunch of tests written for this layout in androidTest/ folder
    val configuration = LocalConfiguration.current
    SubcomposeLayout(modifier) { constraints ->
        // By default `height = 1.25 * width` in portrait and `width = 1.25 * height` in landscape
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val aspectRatio = if (isPortrait) 0.8f else 1.25f

        // Incoming constraints must have known width, as we will calculate height based on it
        val layoutWidth: Int = constraints.maxWidth
        require(layoutWidth != Constraints.Infinity)

        // We must not exceed screen height and also leave some safe margin to be able to see next list items
        val maxHeight = (configuration.screenHeightDp.dp - 144.dp).roundToPx()
        val topPadding = 16.dp
        val availableHeight: Int = (layoutWidth / aspectRatio + topPadding.roundToPx()).roundToInt()
            .coerceAtMost(maxHeight)

        // Some useful constants
        val buttonsHeight = 130.dp + 16.sp.toDp()
        val coverMargin = 36.dp
        val coverMarginTop = coverMargin + topPadding
        val coverMarginBottom = 16.dp
        val titlePadding = 8.dp

        // These values are true for Medium_24_28 font
        val titleTwoLinesHeight = 60.sp.toDp()

        // We have to decide if we will hide cover or not. Let's start by measuring description
        val contentConstraints =
            Constraints(maxWidth = layoutWidth).offset(horizontal = -48.dp.roundToPx()) // add paddings
        val descriptionPlaceable = description?.let {
            subcompose(SlotsEnum.Description, it).single().measure(contentConstraints)
        }
        val descriptionHeight = (descriptionPlaceable?.height?.toDp()
            ?: 12.dp) // if no description - replace it with some space

        val availableSpaceForCoverAndContent =
            availableHeight.toDp() - buttonsHeight - coverMarginTop - coverMarginBottom
        // Now we calculate available space for cover (without measuring the title - we just assume it will have two lines of text)
        val contentMaxPossibleHeight = titleTwoLinesHeight + titlePadding + descriptionHeight
        val availableSpaceForCover = availableSpaceForCoverAndContent - contentMaxPossibleHeight

        // We consider cover too small if it is smaller than 96.dp
        val showCover = availableSpaceForCover >= 96.dp

        val descriptionPadding: Dp
        val titlePlaceable = if (showCover) {
            // If cover is not small - measure text with two lines
            descriptionPadding = 0.dp
            val titleMeasurable =
                subcompose(SlotsEnum.Title) { HeaderTitleScope(isTwoLine = true).title() }.single()
            val titleConstraints = contentConstraints.copy()
            titleMeasurable.measure(titleConstraints)
        } else {
            // Otherwise title will be in "toolbar" - measure it with one line and a huge padding (for buttons)
            descriptionPadding = 4.dp
            val titleMeasurable =
                subcompose(SlotsEnum.Title) { HeaderTitleScope(isTwoLine = false).title() }.single()
            val titleConstraints = Constraints(maxWidth = layoutWidth)
                .offset(horizontal = -104.dp.roundToPx())
            titleMeasurable.measure(titleConstraints)
        }

        val actualContentHeight =
            titlePlaceable.height.toDp() + titlePadding + descriptionPadding + descriptionHeight

        // Now that we measured title and description - we can finally determine cover size
        val coverHeight: Dp = (availableSpaceForCoverAndContent - actualContentHeight)
            .coerceAtMost(layoutWidth.toDp() - coverMargin * 2) // simulate horizontal padding
            .coerceAtMost(320.dp) // don't let cover grow extremely large

        val titleY: Int = if (showCover) {
            // below cover
            (coverHeight + coverMarginTop + coverMarginBottom).roundToPx()
        } else {
            // in "toolbar"
            val titleBaseline = titlePlaceable[LastBaseline]
            (topPadding + 16.dp + 24.dp).roundToPx() - titleBaseline
        }

        // Calculate final height (may exceed availableHeight)
        val layoutHeight = titleY + actualContentHeight.roundToPx() + buttonsHeight.roundToPx()

        // Center all components horizontally
        val alignment = Alignment.CenterHorizontally

        // Do not place cover if it's too small or is not provided
        val coverPlaceable = if (showCover && cover != null) {
            val coverMeasurable =
                subcompose(SlotsEnum.Cover, cover).single() // Must be exactly one measurable
            if (isCoverBranded) {
                coverMeasurable.measure(Constraints.fixed(layoutWidth, layoutHeight))
            } else {
                coverMeasurable.measure(
                    Constraints.fixed(
                        coverHeight.roundToPx(),
                        coverHeight.roundToPx()
                    )
                )
            }
        } else {
            null
        }

        // Draw top cover on top of image cover
        val topCoverPlaceable = if (showCover && topCover != null) {
            val measurables = subcompose(SlotsEnum.TopCover, topCover)
            val coverMeasurable = when (measurables.size) {
                0 -> null
                1 -> measurables[0]
                else -> throw IllegalArgumentException("List has more than one element.")
            }
            coverMeasurable?.measure(Constraints.fixed(layoutWidth, layoutHeight))
        } else {
            null
        }

        // Only used in previews
        val buttonsPlaceable = buttons?.let {
            subcompose(
                SlotsEnum.Buttons,
                it
            ).single()
        }  // Must be exactly one measurable
            ?.measure(Constraints(maxHeight = buttonsHeight.roundToPx()))

        layout(layoutWidth, layoutHeight) {
            val offsetY =
                coverOffset() // read cover offset in placement block (like in Modifier.offset { ... })
            coverPlaceable?.place(
                x = alignment.align(coverPlaceable.width, layoutWidth, layoutDirection),
                y = (if (isCoverBranded) 0 else coverMarginTop.roundToPx()) + offsetY
            )
            topCoverPlaceable?.place(
                x = 0,
                y = offsetY
            )
            titlePlaceable.place(
                x = alignment.align(titlePlaceable.width, layoutWidth, layoutDirection),
                y = titleY
            )
            descriptionPlaceable?.place(
                x = alignment.align(descriptionPlaceable.width, layoutWidth, layoutDirection),
                y = titleY + titlePlaceable.height + (titlePadding + descriptionPadding).roundToPx()
            )
            buttonsPlaceable?.place(
                x = alignment.align(buttonsPlaceable.width, layoutWidth, layoutDirection),
                y = layoutHeight - 124.dp.roundToPx()
            )
        }
    }
}

private enum class SlotsEnum { Cover, TopCover, Title, Description, Buttons }