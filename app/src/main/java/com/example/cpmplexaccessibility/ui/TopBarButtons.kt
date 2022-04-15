package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cpmplexaccessibility.R

@Composable
fun TopBarButtons(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onOverflowClick: () -> Unit,
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
    ) {
        IconButton(
            modifier = Modifier
                .testTag("go_back")
                .align(Alignment.TopStart)
                .size(48.dp),
            onClick = onBackClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                contentDescription = "Go back"
            )
        }

        Row(Modifier.align(Alignment.TopEnd)) {
            IconButton(
                modifier = Modifier
                    .testTag("overflow")
                    .size(48.dp),
                onClick = onOverflowClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = "show more"
                )
            }
        }
    }
}