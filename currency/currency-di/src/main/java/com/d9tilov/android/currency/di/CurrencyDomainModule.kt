package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.UpdateCurrencyInteractor
import com.d9tilov.android.currency.domain.impl.CurrencyInteractorImpl
import com.d9tilov.android.currency.domain.impl.UpdateCurrencyInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CurrencyDomainModule {

    @Provides
    fun provideCurrencyInteractor(currencyRepo: CurrencyRepo): CurrencyInteractor =
        CurrencyInteractorImpl(currencyRepo)

    @Provides
    fun provideUpdateCurrencyInteractor(currencyInteractor: CurrencyInteractor): UpdateCurrencyInteractor =
        UpdateCurrencyInteractorImpl(currencyInteractor)

}
