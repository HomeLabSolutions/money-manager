package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.currency.data.contract.model.GeocodingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorGeocodeApi(
    private val client: HttpClient,
) : GeocodeApi {
    override suspend fun getCurrencies(
        coords: String,
        key: String,
    ): GeocodingResponse =
        client
            .get("geocode/v1/json") {
                parameter("q", coords)
                parameter("key", key)
            }.body()
}
