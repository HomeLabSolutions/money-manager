package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.CurrencyDataRepo
import com.d9tilov.android.currency.data.impl.CurrencyLocalSource
import com.d9tilov.android.currency.data.impl.GeocodeDataRepo
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
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
object CurrencyDataModule {
    @Provides
    @Singleton
    fun provideCurrencySource(
        appDatabase: AppDatabase,
        preferencesStore: PreferencesStore,
    ): CurrencySource =
        CurrencyLocalSource(
            preferencesStore,
            appDatabase.currencyDao(),
            appDatabase.mainCurrencyDao(),
        )

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

    @Provides
    @Singleton
    fun provideCurrencyRepo(
        currencySource: CurrencySource,
        currencyApi: CurrencyApi,
    ): CurrencyRepo =
        CurrencyDataRepo(
            currencySource,
            currencyApi,
        )

    @Provides
    @Singleton
    fun provideGeoRepo(
        geocodeApi: GeocodeApi,
        preferencesStore: PreferencesStore,
    ): GeocodeRepo = GeocodeDataRepo(geocodeApi, preferencesStore)
}
