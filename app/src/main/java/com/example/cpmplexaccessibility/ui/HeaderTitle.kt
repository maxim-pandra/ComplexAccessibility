package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

class HeaderTitleScope(val isTwoLine: Boolean)

@Composable
fun HeaderTitleScope.HeaderTitle(
    title: String,
    onOverflowedTextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var hasVisualOverflow by remember { mutableStateOf(false) }
    Text(
        title,
        maxLines = if (isTwoLine) 2 else 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        onTextLayout = { hasVisualOverflow = it.hasVisualOverflow },
        modifier = modifier.testTag("title") then if (hasVisualOverflow) {
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onOverflowedTextClick
            )
        } else {
            Modifier
        },
    )
}
