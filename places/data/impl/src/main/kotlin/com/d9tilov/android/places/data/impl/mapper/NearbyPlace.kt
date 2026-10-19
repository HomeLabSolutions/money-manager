package com.d9tilov.android.places.data.impl.mapper

import com.d9tilov.android.places.data.contract.model.GeoapifyPlaceProperties
import com.d9tilov.android.places.data.contract.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.impl.NEARBY_PLACES_LIMIT
import com.d9tilov.android.places.domain.model.NearbyPlace

internal fun GeoapifyPlacesResponse.toNearbyPlaces(): List<NearbyPlace> =
    features
        .mapNotNull { it.properties.toNearbyPlace() }
        .distinctBy { it.id }
        .take(NEARBY_PLACES_LIMIT)

private fun GeoapifyPlaceProperties.toNearbyPlace(): NearbyPlace? {
    val id = id.orEmpty().trim()
    val name = name.orEmpty().trim()
    val distance = distance
    if (id.isBlank() || name.isBlank() || distance == null) return null
    if (!distance.isFinite() || distance < 0) return null
    return NearbyPlace(
        id = id,
        name = name,
        address = address.orEmpty().trim(),
        distanceMeters = distance.toInt(),
    )
}
