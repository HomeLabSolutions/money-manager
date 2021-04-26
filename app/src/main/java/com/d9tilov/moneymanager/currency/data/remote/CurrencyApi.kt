package com.d9tilov.moneymanager.currency.data.remote

import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.currency.data.remote.entity.CurrencyResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("{api_key}/latest/{base}")
    fun getCurrencies(
        @Path("base") baseCurrency: String,
        @Path("api_key") key: String = BuildConfig.API_KEY
    ): Single<CurrencyResponse>
}
