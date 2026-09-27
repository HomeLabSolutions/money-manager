package com.d9tilov.android.insights.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun InsightsConsentDialog(
    visible: Boolean,
    isSaving: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        title = { Text(stringResource(R.string.insights_consent_title)) },
        text = { Text(stringResource(R.string.insights_consent_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isSaving) {
                Text(stringResource(R.string.insights_consent_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text(stringResource(R.string.insights_consent_dismiss))
            }
        },
    )
}
