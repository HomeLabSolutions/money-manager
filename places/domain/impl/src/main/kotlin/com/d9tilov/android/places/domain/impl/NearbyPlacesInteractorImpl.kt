package com.d9tilov.android.places.domain.impl

import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.NearbyPlace

class NearbyPlacesInteractorImpl(
    private val nearbyPlacesRepo: NearbyPlacesRepo,
) : NearbyPlacesInteractor {
    override suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace> =
        nearbyPlacesRepo.getNearbyPlaces(location)
}
