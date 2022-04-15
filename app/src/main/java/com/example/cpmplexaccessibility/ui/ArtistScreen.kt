package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.cpmplexaccessibility.ui.theme.MarkRoundButtonWithText
import com.example.cpmplexaccessibility.ui.theme.PlayRoundButtonWithText
import com.example.cpmplexaccessibility.ui.theme.ShareRoundButtonWithText
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ArtistScreen() {
    val lazyListState = rememberLazyListState()

    val scrollState = remember(lazyListState) { HeaderScreenScrollState(lazyListState) }

    val flingBehavior = ScrollableDefaults.autoScrollFlingBehavior(
        lazyListState = lazyListState,
        additionalOffset = { TopBarHeight.roundToPx() }
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        Modifier
            .fillMaxSize()
            .testTag("artist_screen_success")
    ) {
        LaunchedEffect(Unit) {
            // when we first enter the screen - trigger auto scroll to prevent header from being in inconsistent state
            lazyListState.scroll { with(flingBehavior) { performFling(0f) } }
        }
        var lazyColumnSize by remember { mutableStateOf(0) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { lazyColumnSize = it.height }
                .testTag("lazy_column"),
            state = lazyListState,
            flingBehavior = flingBehavior,
        ) {
            item {
                Box(Modifier) {
                    val backgroundColor = MaterialTheme.colors.surface
                    Box(
                        Modifier.drawWithContent {
                            drawContent()
                            drawRect(backgroundColor.copy(alpha = scrollState.backgroundOverlayAlpha))
                        }
                    ) {
                        ArtistHeader(
                            scrollState = scrollState,
                            onOverflowedTextClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Overflowed text snack")
                                }
                            },
                            onCoverClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Cover snack")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    BottomSheetCap(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            }

            item {
                ListItem("Title1", "subtitle1")
            }
            item {
                ListItem("Title2", "subtitle2")
            }
            item {
                ListItem("Title3", "subtitle3")
            }
            item {
                ListItem("Title4", "subtitle4")
            }
            item {
                ListItem("Title5", "subtitle5")
            }
            item {
                ListItem("Title6", "subtitle6")
            }
            item {
                ListItem("Title7", "subtitle7")
            }
            item {
                ListItem("Title8", "subtitle8")
            }

            item {
                Box(
                    Modifier
                        .height(24.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                )
            }

            item(key = "bottom_padding") {
                // calculate height of all visible items except header and this item
                val itemsHeight = lazyListState.layoutInfo.visibleItemsInfo
                    .filter { it.index != 0 && it.key != "bottom_padding" }
                    .sumOf { it.size }

                // fill the remaining space, but subtract tiny margin of TopBarHeight - it's where flingBehavior stops
                val height =
                    with(LocalDensity.current) { (lazyColumnSize - itemsHeight).toDp() } - TopBarHeight

                Box(
                    Modifier
                        .height(height)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    RevealingTopBar(
        scrollState = scrollState,
        title = "Revealing title",
        onBackClick = {
            scope.launch {
                snackbarHostState.showSnackbar("back snack")
            }
        },
        onOverflowClick = {
            scope.launch {
                snackbarHostState.showSnackbar("Search snack")
            }
        },
        modifier = Modifier.testTag("artist_top_bar"),
    )

    AnchoredBox(lazyListState, Modifier.fillMaxWidth()) {
        val itemsAlpha = { scrollState.secondaryButtonsAlpha }
        ThreeButtons(
            itemsAlpha = itemsAlpha,
            modifier = Modifier.align(Alignment.TopCenter),
            leftButton = {
                ShareRoundButtonWithText(
                    onShareClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Share snack")
                        }
                    },
                    alpha = itemsAlpha,
                )
            },
            centerButton = {
                PlayRoundButtonWithText(
                    onShareClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Play snack")
                        }
                    },
                    alpha = itemsAlpha,
                )
            },
            rightButton = {
                MarkRoundButtonWithText(
                    onShareClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Mark snack")
                        }
                    },
                    alpha = itemsAlpha,
                )
            }
        )
    }
}
