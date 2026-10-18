package com.d9tilov.android.places.domain.model

data class Place(
    val location: LocationData = LocationData.EMPTY,
    val name: String? = null,
    val address: String? = null,
)
