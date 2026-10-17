package com.d9tilov.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyPlacesResponse(
    @SerialName("features") val features: List<GeoapifyPlaceFeature>,
)

@Serializable
data class GeoapifyPlaceFeature(
    @SerialName("properties") val properties: GeoapifyPlaceProperties? = null,
)

@Serializable
data class GeoapifyPlaceProperties(
    @SerialName("place_id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("formatted") val address: String? = null,
    @SerialName("distance") val distance: Double? = null,
    @SerialName("lat") val latitude: Double? = null,
    @SerialName("lon") val longitude: Double? = null,
)
