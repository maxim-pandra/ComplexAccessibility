package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays [button] with [text] underneath. Proper content color and text style will be provided
 * for [text].
 */
@Composable
fun RoundButtonWithText(
    modifier: Modifier = Modifier,
    button: @Composable () -> Unit,
    text: @Composable () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        button()

        Spacer(Modifier.height(8.dp))

        text()
    }
}