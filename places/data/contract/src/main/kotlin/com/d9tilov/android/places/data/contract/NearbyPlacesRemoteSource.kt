package com.d9tilov.android.places.data.contract

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.model.GeoapifyPlacesResponse

interface NearbyPlacesRemoteSource {
    suspend fun getNearbyPlaces(
        location: LocationData,
        language: String,
    ): GeoapifyPlacesResponse
}
