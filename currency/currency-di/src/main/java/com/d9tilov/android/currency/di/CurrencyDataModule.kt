package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.CurrencyDataRepo
import com.d9tilov.android.currency.data.impl.CurrencyLocalSource
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.CurrencyApi
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object CurrencyDataModule {

    @Provides
    fun provideCurrencySource(
        @Dispatcher(MoneyManagerDispatchers.IO) dispatcher: CoroutineDispatcher,
        appDatabase: AppDatabase,
        preferencesStore: PreferencesStore
    ): CurrencySource = CurrencyLocalSource(
        dispatcher,
        preferencesStore,
        appDatabase.currencyDao(),
        appDatabase.mainCurrencyDao()
    )

    @Provides
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Provides
    fun provideCurrencyRepo(
        currencySource: CurrencySource,
        currencyApi: CurrencyApi,
        preferencesStore: PreferencesStore
    ): CurrencyRepo =
        CurrencyDataRepo(
            currencySource,
            currencyApi,
            preferencesStore
        )
}
