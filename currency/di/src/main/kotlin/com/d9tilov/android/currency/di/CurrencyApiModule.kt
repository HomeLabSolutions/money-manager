package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.impl.api.CurrencyApi
import com.d9tilov.android.currency.data.impl.api.GeocodeApi
import com.d9tilov.android.currency.data.impl.api.KtorCurrencyApi
import com.d9tilov.android.currency.data.impl.api.KtorGeocodeApi
import com.d9tilov.android.network.di.qualifier.CurrencyNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrencyApiModule {
    @Provides
    @Singleton
    fun provideCurrencyApi(
        @CurrencyNetworkApi client: HttpClient,
    ): CurrencyApi = KtorCurrencyApi(client)

    @Provides
    @Singleton
    fun provideGeoApi(
        @GeoNetworkApi client: HttpClient,
    ): GeocodeApi = KtorGeocodeApi(client)
}
