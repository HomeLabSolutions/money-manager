package com.d9tilov.android.places.data.impl.mapper

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.model.GeoapifyPlaceProperties
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.impl.NEARBY_PLACES_LIMIT
import com.d9tilov.android.places.data.impl.NEARBY_PLACES_RADIUS_METERS
import com.d9tilov.android.places.data.impl.hasValidPlaceCoordinates
import com.d9tilov.android.places.domain.model.NearbyPlace
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS_METERS = 6_371_000.0

internal fun GeoapifyPlacesResponse.toNearbyPlaces(location: LocationData): List<NearbyPlace> =
    features
        .mapNotNull { it.properties.toCandidate(location) }
        .sortedBy { it.distance }
        .distinctBy { it.place.id }
        .take(NEARBY_PLACES_LIMIT)
        .map { it.place }

private fun GeoapifyPlaceProperties.toCandidate(location: LocationData): PlaceCandidate? {
    val id = id.orEmpty().trim()
    val name = name.orEmpty().trim()
    if (id.isBlank() || name.isBlank()) return null
    val distance = distance?.takeIf { it.isFinite() && it >= 0 } ?: distanceFrom(location)
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

private fun GeoapifyPlaceProperties.distanceFrom(location: LocationData): Double {
    val point = LocationData(latitude ?: Double.NaN, longitude ?: Double.NaN)
    if (!point.hasValidPlaceCoordinates()) return Double.NaN
    val latitudeDelta = Math.toRadians(point.latitude - location.latitude)
    val longitudeDelta = Math.toRadians(point.longitude - location.longitude)
    val a =
        sin(latitudeDelta / 2) * sin(latitudeDelta / 2) +
            cos(Math.toRadians(location.latitude)) * cos(Math.toRadians(point.latitude)) *
            sin(longitudeDelta / 2) * sin(longitudeDelta / 2)
    return 2 * EARTH_RADIUS_METERS * asin(sqrt(a.coerceIn(0.0, 1.0)))
}

private data class PlaceCandidate(
    val place: NearbyPlace,
    val distance: Double,
)
