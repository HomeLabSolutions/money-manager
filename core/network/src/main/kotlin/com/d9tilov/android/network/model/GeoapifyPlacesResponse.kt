package com.d9tilov.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyPlacesResponse(
    val features: List<GeoapifyPlaceFeature?>,
)

@Serializable
data class GeoapifyPlaceFeature(
    val properties: GeoapifyPlaceProperties? = null,
)

@Serializable
data class GeoapifyPlaceProperties(
    @SerialName("place_id") val id: String? = null,
    val name: String? = null,
    @SerialName("formatted") val address: String? = null,
    val distance: Double? = null,
    @SerialName("lat") val latitude: Double? = null,
    @SerialName("lon") val longitude: Double? = null,
)
