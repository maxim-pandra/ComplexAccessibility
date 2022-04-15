package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Building block for [PrimaryRoundButton] and [SecondaryRoundButton]
 */
@Composable
private fun GenericRoundButton(
    onClick: () -> Unit,
    enabled: Boolean,
    clickEnabled: Boolean,
    backgroundColor: Color,
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    GenericButton(
        onClick = onClick,
        enabled = enabled,
        clickEnabled = clickEnabled,
        modifier = modifier
            .size(64.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape),
        content = content,
    )
}

/**
 * Displays high-emphasis button.
 *
 * @param enabled Controls the enabled state and appearance of the button. When `false`, this
 * button will not be clickable
 * @param clickEnabled Same as [enabled], but does not affect appearance
 */
@Composable
fun PrimaryRoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    clickEnabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    GenericRoundButton(
        onClick = onClick,
        enabled = enabled,
        clickEnabled = clickEnabled,
        backgroundColor = Color.White,
        modifier = modifier,
        content = content,
    )
}

/**
 * Displays medium-emphasis button.
 *
 * @param enabled Controls the enabled state and appearance of the button. When `false`, this
 * button will not be clickable
 * @param clickEnabled Same as [enabled], but does not affect appearance
 */
// https://www.figma.com/file/WUj7rigEUmtwnve2bcfPk1/Design-System-Dev?node-id=2481%3A2179
@Composable
fun SecondaryRoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    clickEnabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    GenericRoundButton(
        onClick = onClick,
        enabled = enabled,
        clickEnabled = clickEnabled,
        backgroundColor = Color.Gray,
        modifier = modifier,
        content = content,
    )
}