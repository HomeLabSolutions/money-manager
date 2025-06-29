package com.d9tilov.android.currency.di

import com.d9tilov.android.network.CurrencyApi
import com.d9tilov.android.network.GeocodeApi
import com.d9tilov.android.network.di.qualifier.CurrencyNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrencyApiModule {
    @Provides
    @Singleton
    fun provideCurrencyApi(
        @CurrencyNetworkApi retrofit: Retrofit,
    ): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideGeoApi(
        @GeoNetworkApi retrofit: Retrofit,
    ): GeocodeApi = retrofit.create(GeocodeApi::class.java)
}
