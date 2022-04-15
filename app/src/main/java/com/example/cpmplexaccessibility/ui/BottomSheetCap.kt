package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetCap(
    modifier: Modifier = Modifier,
    drawHandle: Boolean = true,
) {
    Box(
        modifier
            .height(16.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        if (drawHandle) {
            Box(
                Modifier
                    .padding(top = 8.dp)
                    .size(60.dp, 4.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}