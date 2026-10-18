package com.d9tilov.android.places.domain.contract

import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.NearbyPlace

interface NearbyPlacesInteractor {
    suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace>
}
