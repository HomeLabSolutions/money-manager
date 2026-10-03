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
        val containerColor =
            if (enabled) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.onTertiaryContainer
            }
        TextButton(
            onClick = onClick,
            small = true,
            colors =
                ButtonDefaults.textButtonColors(
                    containerColor = containerColor,
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
