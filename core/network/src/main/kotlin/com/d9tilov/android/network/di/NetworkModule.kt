package com.d9tilov.android.network.di

import com.d9tilov.android.network.createNetworkClient
import com.d9tilov.android.network.di.qualifier.CurrencyNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_CURRENCY_URL = "https://v6.exchangerate-api.com/"
    private const val BASE_GEOCODE_URL = "https://api.opencagedata.com/"
    private const val BASE_GEOAPIFY_URL = "https://api.geoapify.com/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideHttpEngine(): HttpClientEngine = Android.create()

    @CurrencyNetworkApi
    @Provides
    @Singleton
    fun provideCurrencyClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient = createNetworkClient(engine, BASE_CURRENCY_URL, json)

    @GeoNetworkApi
    @Provides
    @Singleton
    fun provideGeoClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient = createNetworkClient(engine, BASE_GEOCODE_URL, json)

    @GeoapifyNetworkApi
    @Provides
    @Singleton
    fun provideGeoapifyClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient = createNetworkClient(engine, BASE_GEOAPIFY_URL, json)
}
