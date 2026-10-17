package com.d9tilov.android.transaction.domain.contract

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.transaction.domain.model.NearbyPlace

interface NearbyPlacesRepo {
    suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace>
}
