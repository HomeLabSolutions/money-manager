package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.impl.CurrencyInteractorImpl
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.observer.impl.CurrencyUpdateObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyDomainModule {
    @Binds
    fun provideCurrencyInteractor(impl: CurrencyInteractorImpl): CurrencyInteractor

    @Binds
    fun provideCurrencyObserver(impl: CurrencyUpdateObserverImpl): CurrencyUpdateObserver
}
