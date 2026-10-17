package com.d9tilov.android.network

import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GeoapifyApi {
    @GET("v2/places")
    suspend fun getPlaces(
        @QueryMap parameters: Map<String, String>,
        @Query("apiKey") key: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyPlacesResponse
}
