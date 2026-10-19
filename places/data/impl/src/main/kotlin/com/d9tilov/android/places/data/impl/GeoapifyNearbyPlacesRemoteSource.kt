package com.d9tilov.android.places.data.impl

import com.d9tilov.android.places.data.contract.NearbyPlacesRemoteSource
import com.d9tilov.android.places.data.contract.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.impl.api.GeoapifyApi
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.NEARBY_PLACES_RADIUS_METERS
import java.util.Locale
import javax.inject.Inject

class GeoapifyNearbyPlacesRemoteSource @Inject constructor(
    private val geoapifyApi: GeoapifyApi,
) : NearbyPlacesRemoteSource {
    override suspend fun getNearbyPlaces(location: LocationData): GeoapifyPlacesResponse {
        val language =
            Locale.getDefault().language.takeIf { it.length == LANGUAGE_CODE_LENGTH } ?: Locale.ENGLISH.language
        return geoapifyApi.getPlaces(
            filter = "circle:${location.longitude},${location.latitude},$NEARBY_PLACES_RADIUS_METERS",
            bias = "proximity:${location.longitude},${location.latitude}",
            language = language,
            limit = NEARBY_PLACES_LIMIT,
            categories = PLACE_CATEGORIES.joinToString(","),
            conditions = "named",
        )
    }

    private companion object {
        const val LANGUAGE_CODE_LENGTH = 2
        val PLACE_CATEGORIES =
            setOf(
                "catering",
                "commercial",
                "service",
                "healthcare",
                "entertainment",
                "parking",
                "pet",
                "rental",
            )
    }
}

internal const val NEARBY_PLACES_LIMIT = 20
