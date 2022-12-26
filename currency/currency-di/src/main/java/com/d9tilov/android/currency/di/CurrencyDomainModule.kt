package com.d9tilov.android.currency.di

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.currency.data.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.impl.CurrencyInteractorImpl
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.observer.impl.CurrencyUpdateObserverImpl
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
    fun provideCurrencyObserver(
        currencyInteractor: CurrencyInteractor,
        budgetInteractor: BudgetInteractor
    ): CurrencyUpdateObserver = CurrencyUpdateObserverImpl(currencyInteractor, budgetInteractor)

}
