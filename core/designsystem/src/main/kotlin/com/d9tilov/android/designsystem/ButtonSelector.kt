package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d9tilov.android.designsystem.component.ThemePreviews
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun ButtonSelector(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        val containerColor = MaterialTheme.colorScheme.tertiaryContainer
        TextButton(
            onClick = onClick,
            small = true,
            colors =
                ButtonDefaults.textButtonColors(
                    containerColor =
                        if (enabled) {
                            containerColor
                        } else {
                            MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                alpha = MmTagDefaults.DISABLED_TOPIC_TAG_CONTAINER_ALPHA,
                            )
                        },
                    contentColor = contentColorFor(backgroundColor = containerColor),
                ),
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                text()
            }
        }
    }
}

@ThemePreviews
@Composable
fun ButtonSelectorPreview() {
    MoneyManagerTheme {
        ButtonSelector(onClick = {}) {
            Text("Month")
        }
    }
}

object MmTagDefaults {
    // Button disabled container alpha value not exposed by ButtonDefaults
    const val DISABLED_TOPIC_TAG_CONTAINER_ALPHA = 0.12f
}
