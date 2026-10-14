package com.d9tilov.android.transaction.ui

data class TransactionPlaceActions(
    val onConfirm: () -> Unit = {},
    val onSkip: () -> Unit = {},
    val onEdit: () -> Unit = {},
    val onNameChanged: (String) -> Unit = {},
    val onRetry: () -> Unit = {},
)
