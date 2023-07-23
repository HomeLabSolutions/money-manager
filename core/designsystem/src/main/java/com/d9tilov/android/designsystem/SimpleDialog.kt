package com.d9tilov.android.designsystem

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SimpleDialog(
    show: Boolean,
    title: String,
    subtitle: String,
    confirmButton: String,
    dismissButton: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!show) return
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) { Text(text = confirmButton) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text(text = dismissButton) }
        },
        title = { Text(text = title) },
        text = { Text(text = subtitle) }
    )
}
