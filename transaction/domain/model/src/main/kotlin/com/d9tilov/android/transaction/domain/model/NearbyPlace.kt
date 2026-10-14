package com.d9tilov.android.transaction.domain.model

data class NearbyPlace(
    val id: String,
    val name: String,
    val address: String,
    val distanceMeters: Int,
)
