package com.d9tilov.android.places.data.impl.mapper

import com.d9tilov.android.network.model.GeoapifyPlaceProperties
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.impl.NEARBY_PLACES_LIMIT
import com.d9tilov.android.places.data.impl.NEARBY_PLACES_RADIUS_METERS
import com.d9tilov.android.places.domain.model.NearbyPlace

internal fun GeoapifyPlacesResponse.toNearbyPlaces(): List<NearbyPlace> =
    features
        .mapNotNull { it.properties.toCandidate() }
        .sortedBy { it.distance }
        .distinctBy { it.place.id }
        .take(NEARBY_PLACES_LIMIT)
        .map { it.place }

private fun GeoapifyPlaceProperties.toCandidate(): PlaceCandidate? {
    val id = id.orEmpty().trim()
    val name = name.orEmpty().trim()
    val distance = distance
    if (id.isBlank() || name.isBlank() || distance == null) return null
    if (distance !in 0.0..NEARBY_PLACES_RADIUS_METERS.toDouble()) return null
    return PlaceCandidate(
        place =
            NearbyPlace(
                id = id,
                name = name,
                address = address.orEmpty().trim(),
                distanceMeters = distance.toInt(),
            ),
        distance = distance,
    )
}

private data class PlaceCandidate(
    val place: NearbyPlace,
    val distance: Double,
)
