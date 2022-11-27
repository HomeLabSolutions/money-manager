package com.d9tilov.moneymanager.currency.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.currency.data.CurrencyDataRepo
import com.d9tilov.moneymanager.currency.data.local.CurrencyCacheSource
import com.d9tilov.moneymanager.currency.data.local.CurrencyCacheSourceImpl
import com.d9tilov.moneymanager.currency.data.local.CurrencyLocalSource
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractorImpl
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import com.d9tilov.moneymanager.currency.domain.UpdateCurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.UpdateCurrencyInteractorImpl
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrencyModule {

    @Provides
    @Singleton
    fun provideCurrencySource(
        appDatabase: AppDatabase,
        preferencesStore: PreferencesStore,
    ): CurrencySource = CurrencyLocalSource(
        preferencesStore,
        appDatabase.currencyDao(),
        appDatabase.mainCurrencyDao()
    )

    @Provides
    @Singleton
    fun provideCurrencyCacheSource(): CurrencyCacheSource = CurrencyCacheSourceImpl()

    @Provides
    @Singleton
    fun provideCurrencyRepo(
        currencySource: CurrencySource,
        currencyCacheSource: CurrencyCacheSource,
        retrofit: Retrofit,
        preferencesStore: PreferencesStore
    ): CurrencyRepo =
        CurrencyDataRepo(
            currencySource,
            currencyCacheSource,
            retrofit.create(CurrencyApi::class.java),
            preferencesStore
        )

    @Provides
    @Singleton
    fun provideCurrencyInteractor(
        currencyRepo: CurrencyRepo,
        domainMapper: CurrencyDomainMapper
    ): CurrencyInteractor = CurrencyInteractorImpl(currencyRepo, domainMapper)

    @Provides
    fun provideUpdateCurrencyInteractor(
        currencyInteractor: CurrencyInteractor,
        budgetInteractor: BudgetInteractor,
    ): UpdateCurrencyInteractor = UpdateCurrencyInteractorImpl(currencyInteractor, budgetInteractor)

}
