package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import com.d9tilov.android.transaction.regular.domain.impl.RegularTransactionInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RegularTransactionDomainModule {
    @Provides
    fun provideRegularTransactionInteractor(
        regularTransactionRepo: RegularTransactionRepo,
        currencyInteractor: CurrencyInteractor,
        categoryInteractor: CategoryInteractor,
    ): RegularTransactionInteractor =
        RegularTransactionInteractorImpl(
            currencyInteractor,
            regularTransactionRepo,
            categoryInteractor,
        )
}
