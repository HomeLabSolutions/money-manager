package com.d9tilov.android.places.data.contract

import com.d9tilov.android.places.data.contract.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.domain.model.LocationData

interface NearbyPlacesRemoteSource {
    suspend fun getNearbyPlaces(location: LocationData): GeoapifyPlacesResponse
}
