package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d9tilov.android.designsystem.component.ThemePreviews
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun ButtonSelector(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean = true,
    text: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        val colorScheme = MaterialTheme.colorScheme
        TextButton(
            onClick = onClick,
            small = true,
            colors =
                ButtonDefaults.textButtonColors(
                    containerColor = if (selected) colorScheme.secondaryContainer else colorScheme.surfaceContainerHigh,
                    contentColor = if (selected) colorScheme.onSecondaryContainer else colorScheme.onSurfaceVariant,
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
        Column {
            ButtonSelector(onClick = {}, selected = true) {
                Text("Month")
            }
            ButtonSelector(onClick = {}, selected = false) {
                Text("Year")
            }
        }
    }
}
