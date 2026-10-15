package com.d9tilov.android.transaction.ui

data class TransactionPlaceActions(
    val onEdit: () -> Unit = {},
    val onNameChanged: (String) -> Unit = {},
)
