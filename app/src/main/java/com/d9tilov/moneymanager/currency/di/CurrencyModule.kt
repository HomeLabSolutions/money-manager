package com.d9tilov.moneymanager.currency.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.currency.data.CurrencyDataRepo
import com.d9tilov.moneymanager.currency.data.local.CurrencyLocalSource
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.local.mapper.CurrencyLocalMapper
import com.d9tilov.moneymanager.currency.data.remote.mapper.CurrencyRemoteMapper
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractorImpl
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class CurrencyModule {

    @Provides
    @ActivityRetainedScoped
    fun provideCurrencySource(
        currencyLocalMapper: CurrencyLocalMapper,
        appDatabase: AppDatabase
    ): CurrencySource = CurrencyLocalSource(currencyLocalMapper, appDatabase)

    @Provides
    @ActivityRetainedScoped
    fun provideCurrencyRepo(
        currencySource: CurrencySource,
        currencyRemoteMapper: CurrencyRemoteMapper,
        retrofit: Retrofit
    ): CurrencyRepo =
        CurrencyDataRepo(currencySource, currencyRemoteMapper, retrofit)

    @Provides
    @ActivityRetainedScoped
    fun provideCurrencyInteractor(
        currencyRepo: CurrencyRepo,
        userInteractor: UserInteractor,
        domainMapper: CurrencyDomainMapper
    ): CurrencyInteractor = CurrencyInteractorImpl(currencyRepo, userInteractor, domainMapper)
}
