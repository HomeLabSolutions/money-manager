package com.d9tilov.android.core.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val EMPTY = LocationData(0.0, 0.0)
    }
}
