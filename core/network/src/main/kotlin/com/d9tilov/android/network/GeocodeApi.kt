package com.d9tilov.android.network

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.network.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeocodeApi {
    @GET("geocode/v1/json")
    suspend fun getCurrencies(
        @Query("q") baseCurrency: String = DEFAULT_CURRENCY_CODE,
        @Query("api_key") key: String = BuildConfig.CURRENCY_API_KEY,
    ): CurrencyResponse
}