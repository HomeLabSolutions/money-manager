package com.d9tilov.android.transaction.ui

import com.d9tilov.android.places.domain.model.NearbyPlace

data class TransactionPlaceActions(
    val onEdit: () -> Unit = {},
    val onDismiss: () -> Unit = {},
    val onNameChanged: (String) -> Unit = {},
    val onPlaceSelected: (NearbyPlace) -> Unit = {},
)
