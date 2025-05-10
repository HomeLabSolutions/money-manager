package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.CurrencyDataRepo
import com.d9tilov.android.currency.data.impl.CurrencyLocalSource
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.CurrencyApi
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
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideCurrencyRepo(
        currencySource: CurrencySource,
        currencyApi: CurrencyApi,
        preferencesStore: PreferencesStore,
    ): CurrencyRepo =
        CurrencyDataRepo(
            currencySource,
            currencyApi,
            preferencesStore,
        )
}
