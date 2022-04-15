package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cpmplexaccessibility.R

@Composable
fun ThreeButtons(
    modifier: Modifier = Modifier,
    itemsAlpha: () -> Float,
    leftButton: @Composable () -> Unit,
    centerButton: @Composable () -> Unit,
    rightButton: @Composable () -> Unit,
) {
    val widthModifier =
        Modifier.width((64 + 2 * 20).dp) // where 64 is the width of the button, and 20 is padding
    Row(modifier = modifier) {
        Box(
            widthModifier.alpha { itemsAlpha() },
            contentAlignment = Alignment.TopCenter,
        ) {
            leftButton()
        }
        Box(
            widthModifier,
            contentAlignment = Alignment.TopCenter,
        ) {
            centerButton()
        }
        Box(
            widthModifier.alpha { itemsAlpha() },
            contentAlignment = Alignment.TopCenter,
        ) {
            rightButton()
        }
    }
}

@Preview
@Composable
private fun ThreeButtonsPreview() {
    Box(
        Modifier
            .background(Color(0XFF2A5F79))
            .padding(vertical = 32.dp)
    ) {
        ThreeButtonsForPreview()
    }
}

@Composable
fun ThreeButtonsForPreview(modifier: Modifier = Modifier) {
    ThreeButtons(
        itemsAlpha = { 1f },
        modifier = modifier,
        leftButton = {
            RoundButtonWithText(
                button = {
                    SecondaryRoundButton({}) {
                        Icon(painterResource(id = R.drawable.ic_baseline_bookmark_24), null, tint = Color.White)
                    }
                },
                text = { Text("Button 1") }
            )
        },
        centerButton = {
            RoundButtonWithText(
                button = {
                    PrimaryRoundButton({}) {
                        Icon(painterResource(R.drawable.ic_baseline_play_arrow_24), null)
                    }
                },
                text = { Text("Button 2") }
            )
        },
        rightButton = {
            RoundButtonWithText(
                button = {
                    SecondaryRoundButton({}) {
                        Icon(painterResource(id = R.drawable.ic_baseline_share_24), null, tint = Color.White)
                    }
                },
                text = { Text("Button 3") }
            )
        }
    )
}
