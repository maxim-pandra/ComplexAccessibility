package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ArtistHeader(
    scrollState: HeaderScreenScrollState,
    onOverflowedTextClick: () -> Unit,
    onCoverClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
    ) {
        HeaderLayout(
            coverOffset = { scrollState.scrollPx },
            cover = {
                Box(modifier = Modifier
                    .clickable(
                        onClick = onCoverClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                    .background(Color.Blue)
                    .testTag("artist_cover")
                    .semantics { invisibleToUser() })

            },
            title = {
                val alphaModifier = Modifier.alpha { scrollState.titleAlpha }

                HeaderTitle(
                    title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                    onOverflowedTextClick = onOverflowedTextClick,
                    modifier = alphaModifier,
                )
            },
            isCoverBranded = false
        )
    }
}
