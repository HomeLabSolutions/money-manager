package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.currency.data.contract.model.GeocodingResponse
import com.d9tilov.android.currency.data.impl.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
    @GET("geocode/v1/json")
    suspend fun getCurrencies(
        @Query("q") coords: String,
        @Query("key") key: String = BuildConfig.GEOCODING_API_KEY,
    ): GeocodingResponse
}
