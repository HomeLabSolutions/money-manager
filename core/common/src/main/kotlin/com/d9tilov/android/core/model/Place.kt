package com.d9tilov.android.core.model

data class Place(
    val location: LocationData = LocationData.EMPTY,
    val name: String? = null,
    val address: String? = null,
)
