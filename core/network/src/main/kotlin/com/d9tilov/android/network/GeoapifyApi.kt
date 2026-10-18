package com.d9tilov.android.network

import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoapifyApi {
    @GET("v2/places")
    suspend fun getPlaces(
        @Query("filter") filter: String,
        @Query("bias") bias: String,
        @Query("lang") language: String,
        @Query("limit") limit: Int,
        @Query("categories") categories: String,
        @Query("conditions") conditions: String,
        @Query("apiKey") key: String = BuildConfig.GEOAPIFY_API_KEY,
    ): GeoapifyPlacesResponse
}
