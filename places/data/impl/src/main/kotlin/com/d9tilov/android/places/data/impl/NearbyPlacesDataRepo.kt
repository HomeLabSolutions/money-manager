package com.d9tilov.android.places.data.impl

import com.d9tilov.android.places.data.contract.NearbyPlacesRemoteSource
import com.d9tilov.android.places.data.impl.mapper.toNearbyPlaces
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.NearbyPlace
import dev.zacsweers.metro.Inject

@Inject
class NearbyPlacesDataRepo(
    private val remoteSource: NearbyPlacesRemoteSource,
) : NearbyPlacesRepo {
    override suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace> =
        remoteSource.getNearbyPlaces(location).toNearbyPlaces()
}
