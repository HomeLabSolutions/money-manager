package com.d9tilov.android.places.data.impl.api

import com.d9tilov.android.places.data.contract.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.impl.BuildConfig

interface GeoapifyApi {
    suspend fun getPlaces(
        filter: String,
        bias: String,
        language: String,
        limit: Int,
        categories: String,
        conditions: String,
        key: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyPlacesResponse
}
