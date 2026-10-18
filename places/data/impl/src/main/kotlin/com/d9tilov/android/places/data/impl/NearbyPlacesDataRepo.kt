package com.d9tilov.android.places.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.places.data.contract.NearbyPlacesRemoteSource
import com.d9tilov.android.places.data.impl.mapper.toNearbyPlaces
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.places.domain.model.NearbyPlace
import javax.inject.Inject

class NearbyPlacesDataRepo @Inject constructor(
    private val remoteSource: NearbyPlacesRemoteSource,
) : NearbyPlacesRepo {
    override suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace> {
        require(location.hasValidPlaceCoordinates())
        return remoteSource.getNearbyPlaces(location).toNearbyPlaces()
    }
}

private const val MIN_LATITUDE = -90.0
private const val MAX_LATITUDE = 90.0
private const val MIN_LONGITUDE = -180.0
private const val MAX_LONGITUDE = 180.0

internal fun LocationData.hasValidPlaceCoordinates(): Boolean =
    latitude.isFinite() && latitude in MIN_LATITUDE..MAX_LATITUDE &&
        longitude.isFinite() && longitude in MIN_LONGITUDE..MAX_LONGITUDE
