package com.example.cpmplexaccessibility.ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun AnchoredBox(
    state: LazyListState,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(top = 46.dp, bottom = 36.dp),
    content: @Composable BoxScope.() -> Unit
) {

    var height by remember { mutableStateOf(0) }

    // https://cubic-bezier.com/#0,0,.5,1, to smoothly transition "f(y) = x" into "f(y) = 0"
    val easing = remember { CubicBezierEasing(.5f, 0f, 1f, 1f) }

    Box(
        content = content,
        modifier = modifier
            .onSizeChanged { height = it.height }
            .padding(paddingValues)
            .offset {
                val backdropBottomY = state.firstItemBottomY()
                val y = backdropBottomY - height
                val threshold = 100
                // offset is a function of y, https://www.desmos.com/calculator/y9pcemle72
                val offset = when {
                    y > threshold -> y
                    y < -threshold -> 0
                    else -> {
                        val normalized = lerp(y.toFloat(), from = -threshold.toFloat()..threshold.toFloat(), to = 0f..1f)
                        (threshold * easing.transform(normalized)).toInt()
                    }
                }
                IntOffset(0, offset)
            }
    )
}
