package com.d9tilov.moneymanager.currency.data.remote

import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.currency.data.remote.entity.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("{api_key}/latest/{base}")
    suspend fun getCurrencies(
        @Path("base") baseCurrency: String = DEFAULT_CURRENCY_CODE,
        @Path("api_key") key: String = BuildConfig.API_KEY
    ): CurrencyResponse
}
