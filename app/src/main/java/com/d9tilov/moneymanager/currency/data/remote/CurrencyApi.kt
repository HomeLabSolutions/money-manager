package com.d9tilov.moneymanager.currency.data.remote

import com.d9tilov.moneymanager.currency.data.remote.entity.CurrencyResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("latest")
    fun getCurrencies(@Query("base") baseCurrency: String): Single<CurrencyResponse>
}
