package com.d9tilov.android.transaction.ui.vm

import com.d9tilov.android.transaction.domain.model.NearbyPlace

data class TransactionPlaceState(
    val places: List<NearbyPlace> = emptyList(),
    val status: NearbyPlacesStatus = NearbyPlacesStatus.IDLE,
    val pickerVisible: Boolean = false,
)

enum class NearbyPlacesStatus {
    IDLE,
    LOADING,
    READY,
    UNAVAILABLE,
    NO_LOCATION,
}
