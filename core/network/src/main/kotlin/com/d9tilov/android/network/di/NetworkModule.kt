package com.d9tilov.android.network.di

import com.d9tilov.android.network.createNetworkClient
import com.d9tilov.android.network.di.qualifier.CurrencyNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import kotlinx.serialization.json.Json

@BindingContainer
@ContributesTo(AppScope::class)
object NetworkModule {
    private const val BASE_CURRENCY_URL = "https://v6.exchangerate-api.com/"
    private const val BASE_GEOCODE_URL = "https://api.opencagedata.com/"
    private const val BASE_GEOAPIFY_URL = "https://api.geoapify.com/"

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpEngine(): HttpClientEngine = Android.create()

    @CurrencyNetworkApi
    @Provides
    @SingleIn(AppScope::class)
    fun provideCurrencyClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient = createNetworkClient(engine, BASE_CURRENCY_URL, json)

    @GeoNetworkApi
    @Provides
    @SingleIn(AppScope::class)
    fun provideGeoClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient = createNetworkClient(engine, BASE_GEOCODE_URL, json)

    @GeoapifyNetworkApi
    @Provides
    @SingleIn(AppScope::class)
    fun provideGeoapifyClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient = createNetworkClient(engine, BASE_GEOAPIFY_URL, json)
}
