package com.d9tilov.android.places.domain.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.places.domain.model.NearbyPlace
import javax.inject.Inject

class NearbyPlacesInteractorImpl @Inject constructor(
    private val nearbyPlacesRepo: NearbyPlacesRepo,
) : NearbyPlacesInteractor {
    override suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace> =
        nearbyPlacesRepo.getNearbyPlaces(location)
}
