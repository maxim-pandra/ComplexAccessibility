package com.example.cpmplexaccessibility.ui.theme

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import com.example.cpmplexaccessibility.R
import com.example.cpmplexaccessibility.ui.RoundButtonWithText
import com.example.cpmplexaccessibility.ui.SecondaryRoundButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareRoundButtonWithText(
    onShareClick: () -> Unit,
    alpha: () -> Float,
) {
    RoundButtonWithText(
        button = {
            SecondaryRoundButton(
                modifier = Modifier.testTag("share_button"),
                onClick = onShareClick,
                clickEnabled = alpha() > 0.5f,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_share_24),
                    contentDescription = "Share",
                )
            }
        },
        text = {
            Text(
                text = "Share",
                maxLines = 1,
                modifier = Modifier.semantics { invisibleToUser() })
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayRoundButtonWithText(
    onShareClick: () -> Unit,
    alpha: () -> Float,
) {
    RoundButtonWithText(
        button = {
            SecondaryRoundButton(
                onClick = onShareClick,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_play_arrow_24),
                    contentDescription = "Play",
                )
            }
        },
        text = {
            Text(
                text = "Play",
                maxLines = 1,
                modifier = Modifier.semantics { invisibleToUser() })
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MarkRoundButtonWithText(
    onShareClick: () -> Unit,
    alpha: () -> Float,
) {
    RoundButtonWithText(
        button = {
            SecondaryRoundButton(
                onClick = onShareClick,
                clickEnabled = alpha() > 0.5f,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_bookmark_24),
                    contentDescription = "Bookmark",
                )
            }
        },
        text = {
            Text(
                text = "Bookmark",
                maxLines = 1,
                modifier = Modifier.semantics { invisibleToUser() })
        },
    )
}
