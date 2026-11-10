package com.d9tilov.android.network.di

import com.d9tilov.android.network.createNetworkClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

private const val BASE_CURRENCY_URL = "https://v6.exchangerate-api.com/"
private const val BASE_GEOCODE_URL = "https://api.opencagedata.com/"
private const val BASE_GEOAPIFY_URL = "https://api.geoapify.com/"

fun provideJson(): Json = Json { ignoreUnknownKeys = true }

fun provideHttpEngine(): HttpClientEngine = Android.create()

fun provideCurrencyClient(
    engine: HttpClientEngine,
    json: Json,
): HttpClient = createNetworkClient(engine, BASE_CURRENCY_URL, json)

fun provideGeoClient(
    engine: HttpClientEngine,
    json: Json,
): HttpClient = createNetworkClient(engine, BASE_GEOCODE_URL, json)

fun provideGeoapifyClient(
    engine: HttpClientEngine,
    json: Json,
): HttpClient = createNetworkClient(engine, BASE_GEOAPIFY_URL, json)

val networkModule =
    module {
        single { create(::provideJson) }
        single { create(::provideHttpEngine) }
        single(named("CurrencyNetworkApi")) { create(::provideCurrencyClient) }
        single(named("GeoNetworkApi")) { create(::provideGeoClient) }
        single(named("GeoapifyNetworkApi")) { create(::provideGeoapifyClient) }
    }
