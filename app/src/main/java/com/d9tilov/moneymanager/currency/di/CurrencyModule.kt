package com.d9tilov.moneymanager.currency.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.CurrencyDataRepo
import com.d9tilov.moneymanager.currency.data.local.CurrencyLocalSource
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractorImpl
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class CurrencyModule {

    @Provides
    fun provideCurrencySource(
        appDatabase: AppDatabase
    ): CurrencySource = CurrencyLocalSource(appDatabase.currencyDao())

    @Provides
    fun provideCurrencyRepo(
        preferencesStore: PreferencesStore,
        currencySource: CurrencySource,
        retrofit: Retrofit
    ): CurrencyRepo =
        CurrencyDataRepo(preferencesStore, currencySource, retrofit.create(CurrencyApi::class.java))

    @Provides
    fun provideCurrencyInteractor(
        currencyRepo: CurrencyRepo,
        domainMapper: CurrencyDomainMapper
    ): CurrencyInteractor = CurrencyInteractorImpl(currencyRepo, domainMapper)
}
