package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.currency.data.contract.model.CurrencyResponse
import com.d9tilov.android.currency.data.impl.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("v6/{api_key}/latest/{base}")
    suspend fun getCurrencies(
        @Path("base") baseCurrency: String = DEFAULT_CURRENCY_CODE,
        @Path("api_key") key: String = BuildConfig.CURRENCY_API_KEY,
    ): CurrencyResponse
}
