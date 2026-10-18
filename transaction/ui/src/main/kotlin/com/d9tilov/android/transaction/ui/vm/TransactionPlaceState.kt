package com.d9tilov.android.transaction.ui.vm

import androidx.annotation.StringRes
import com.d9tilov.android.places.domain.model.NearbyPlace
import com.d9tilov.android.transaction.ui.R

data class TransactionPlaceState(
    val places: List<NearbyPlace> = emptyList(),
    val status: NearbyPlacesStatus = NearbyPlacesStatus.IDLE,
    val pickerVisible: Boolean = false,
) {
    @get:StringRes
    val messageRes: Int?
        get() =
            when (status) {
                NearbyPlacesStatus.UNAVAILABLE -> R.string.transaction_place_unavailable
                NearbyPlacesStatus.NO_LOCATION -> R.string.transaction_place_no_location
                NearbyPlacesStatus.READY -> if (places.isEmpty()) R.string.transaction_place_empty else null
                else -> null
            }
}

enum class NearbyPlacesStatus {
    IDLE,
    LOADING,
    READY,
    UNAVAILABLE,
    NO_LOCATION,
}
