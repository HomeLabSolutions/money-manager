package com.d9tilov.android.interactor_impl.di

import com.d9tilov.android.interactor.CurrencyInteractor
import com.d9tilov.android.interactor.UpdateCurrencyInteractor
import com.d9tilov.android.interactor_impl.CurrencyInteractorImpl
import com.d9tilov.android.interactor_impl.UpdateCurrencyInteractorImpl
import com.d9tilov.android.interactor_impl.mapper.CurrencyDomainMapper
import com.d9tilov.android.repo.CurrencyRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CurrencyDomainModule {

    @Binds
    fun provideCurrencyInteractor(
        currencyRepo: CurrencyRepo,
        domainMapper: CurrencyDomainMapper
    ): CurrencyInteractor =
        CurrencyInteractorImpl(currencyRepo, domainMapper)

    @Binds
    fun provideUpdateCurrencyInteractor(
        currencyInteractor: CurrencyInteractor,
    ): UpdateCurrencyInteractor = UpdateCurrencyInteractorImpl(currencyInteractor)

}
