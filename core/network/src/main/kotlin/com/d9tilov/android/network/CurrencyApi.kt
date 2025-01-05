package com.d9tilov.android.network

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.network.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("{api_key}/latest/{base}")
    suspend fun getCurrencies(
        @Path("base") baseCurrency: String = DEFAULT_CURRENCY_CODE,
        @Path("api_key") key: String = BuildConfig.CURRENCY_API_KEY,
    ): CurrencyResponse
}
