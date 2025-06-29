package com.d9tilov.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponse(
    @SerialName("results") val results: List<GeocodingResult>,
)

@Serializable
data class GeocodingResult(
    @SerialName("annotations") val annotations: GeocodingAnnotation,
)

@Serializable
data class GeocodingAnnotation(
    @SerialName("currency") val currency: GeocodingCurrency,
)

@Serializable
data class GeocodingCurrency(
    @SerialName("iso_code")
    val isoCode: String,
)
