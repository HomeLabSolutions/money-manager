package com.d9tilov.android.places.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.GeoapifyApi
import com.d9tilov.android.places.data.impl.mapper.toNearbyPlaces
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.places.domain.model.NearbyPlace
import java.util.Locale
import javax.inject.Inject

class NearbyPlacesDataRepo @Inject constructor(
    private val geoapifyApi: GeoapifyApi,
) : NearbyPlacesRepo {
    override suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace> {
        require(location.hasValidPlaceCoordinates())
        val language = Locale.getDefault().language.takeIf { it.matches(LANGUAGE_PATTERN) } ?: "en"
        return geoapifyApi
            .getPlaces(
                filter = "circle:${location.longitude},${location.latitude},$NEARBY_PLACES_RADIUS_METERS",
                bias = "proximity:${location.longitude},${location.latitude}",
                language = language,
                limit = NEARBY_PLACES_LIMIT,
                categories = PLACE_CATEGORIES.joinToString(","),
                conditions = "named",
            ).toNearbyPlaces()
    }

    private companion object {
        val PLACE_CATEGORIES = setOf("catering", "commercial")
        val LANGUAGE_PATTERN = Regex("[a-z]{2,3}")
    }
}

internal const val NEARBY_PLACES_RADIUS_METERS = 150
internal const val NEARBY_PLACES_LIMIT = 20

private const val MIN_LATITUDE = -90.0
private const val MAX_LATITUDE = 90.0
private const val MIN_LONGITUDE = -180.0
private const val MAX_LONGITUDE = 180.0

internal fun LocationData.hasValidPlaceCoordinates(): Boolean =
    latitude.isFinite() && latitude in MIN_LATITUDE..MAX_LATITUDE &&
        longitude.isFinite() && longitude in MIN_LONGITUDE..MAX_LONGITUDE
