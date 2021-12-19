package com.d9tilov.moneymanager.exchanger.di

import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.exchanger.domain.ExchangeInteractor
import com.d9tilov.moneymanager.exchanger.domain.ExchangeInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ExchangerModule {

    @Provides
    fun provideExchangerInteractor(
        currencyInteractor: CurrencyInteractor
    ): ExchangeInteractor = ExchangeInteractorImpl(currencyInteractor)
}
