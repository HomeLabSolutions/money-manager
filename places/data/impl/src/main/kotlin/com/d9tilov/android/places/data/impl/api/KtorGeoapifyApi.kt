package com.d9tilov.android.places.data.impl.api

import com.d9tilov.android.places.data.contract.model.GeoapifyPlacesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorGeoapifyApi(
    private val client: HttpClient,
) : GeoapifyApi {
    override suspend fun getPlaces(
        filter: String,
        bias: String,
        language: String,
        limit: Int,
        categories: String,
        conditions: String,
        key: String,
    ): GeoapifyPlacesResponse =
        client
            .get("v2/places") {
                parameter("filter", filter)
                parameter("bias", bias)
                parameter("lang", language)
                parameter("limit", limit)
                parameter("categories", categories)
                parameter("conditions", conditions)
                parameter("apiKey", key)
            }.body()
}
