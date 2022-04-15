package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

/**
 * @see androidx.compose.ui.draw.alpha
 */
fun Modifier.alpha(alpha: () -> Float): Modifier {
    return graphicsLayer {
        this.clip = true
        this.alpha = alpha()
    }
}

/**
 * Increases clickable area of element without affecting its placement or size
 */
fun Modifier.biggerClickable(onClick: () -> Unit, horizontal: Dp = 0.dp, vertical: Dp = 0.dp): Modifier {
    return unPadding(vertical = vertical, horizontal = horizontal)
        .clickable(onClick = onClick)
        .padding(vertical = vertical, horizontal = horizontal)
}

private fun Modifier.unPadding(horizontal: Dp, vertical: Dp) = this.layout { measurable, constraints ->
    val horizontalPx = horizontal.roundToPx()
    val verticalPx = vertical.roundToPx()
    val newConstraints = constraints.offset(horizontal = horizontalPx, vertical = verticalPx)
    val placeable = measurable.measure(newConstraints)
    layout(placeable.width - horizontalPx * 2, placeable.height - verticalPx * 2) {
        placeable.place(-horizontalPx, -verticalPx)
    }
}

