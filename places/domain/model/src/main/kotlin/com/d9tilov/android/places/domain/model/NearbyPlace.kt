package com.d9tilov.android.places.domain.model

data class NearbyPlace(
    val id: String,
    val name: String,
    val address: String,
    val distanceMeters: Int,
)
