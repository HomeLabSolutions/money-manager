package com.d9tilov.android.repo_impl.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.CurrencyApi
import com.d9tilov.android.repo.CurrencyCacheSource
import com.d9tilov.android.repo.CurrencyRepo
import com.d9tilov.android.repo.CurrencySource
import com.d9tilov.android.repo_impl.CurrencyCacheSourceImpl
import com.d9tilov.android.repo_impl.CurrencyDataRepo
import com.d9tilov.android.repo_impl.CurrencyLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CurrencyDataModule {

    @Binds
    fun provideCurrencySource(
        appDatabase: AppDatabase,
        preferencesStore: PreferencesStore,
    ): com.d9tilov.android.repo.CurrencySource = CurrencyLocalSource(
        preferencesStore,
        appDatabase.currencyDao(),
        appDatabase.mainCurrencyDao()
    )

    @Binds
    fun provideCurrencyCacheSource(): com.d9tilov.android.repo.CurrencyCacheSource = CurrencyCacheSourceImpl()

    @Binds
    fun provideCurrencyRepo(
        currencySource: com.d9tilov.android.repo.CurrencySource,
        currencyCacheSource: com.d9tilov.android.repo.CurrencyCacheSource,
        currencyApi: CurrencyApi,
        preferencesStore: PreferencesStore
    ): com.d9tilov.android.repo.CurrencyRepo =
        CurrencyDataRepo(
            currencySource,
            currencyCacheSource,
            currencyApi,
            preferencesStore
        )
}
