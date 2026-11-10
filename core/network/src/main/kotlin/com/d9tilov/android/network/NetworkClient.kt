package com.d9tilov.android.network

import com.d9tilov.android.core.constants.DataConstants.TAG
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val NETWORK_TIMEOUT_MILLIS = 15_000L

private val networkLogging =
    createClientPlugin("NetworkLogging") {
        onRequest { request, _ ->
            Timber.tag(TAG).d("HTTP REQUEST: %s %s", request.method.value, request.url.host)
        }
        onResponse { response ->
            Timber.tag(TAG).d("HTTP RESPONSE: %s %s", response.status.value, response.call.request.url.host)
        }
    }

fun createNetworkClient(
    engine: HttpClientEngine,
    baseUrl: String,
    json: Json,
): HttpClient =
    HttpClient(engine) {
        expectSuccess = true
        defaultRequest { url(baseUrl) }
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = NETWORK_TIMEOUT_MILLIS
            socketTimeoutMillis = NETWORK_TIMEOUT_MILLIS
        }
        if (BuildConfig.DEBUG) install(networkLogging)
    }
