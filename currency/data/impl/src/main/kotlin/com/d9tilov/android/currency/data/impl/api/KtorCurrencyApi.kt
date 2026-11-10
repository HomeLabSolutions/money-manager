package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.currency.data.contract.model.CurrencyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

class KtorCurrencyApi(
    private val client: HttpClient,
) : CurrencyApi {
    override suspend fun getCurrencies(
        baseCurrency: String,
        key: String,
    ): CurrencyResponse =
        client
            .get {
                url { appendPathSegments("v6", key, "latest", baseCurrency, encodeSlash = true) }
            }.body()
}
