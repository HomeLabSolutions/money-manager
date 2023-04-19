package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.data.contract.TransactionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object TransactionDomainModule {

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionInteractor(
        transactionRepo: TransactionRepo,
        regularTransactionInteractor: RegularTransactionInteractor,
        categoryInteractor: CategoryInteractor,
        userInteractor: UserInteractor,
        currencyInteractor: CurrencyInteractor,
        budgetInteractor: BudgetInteractor
    ): TransactionInteractor = TransactionInteractorImpl(
        transactionRepo,
        regularTransactionInteractor,
        categoryInteractor,
        userInteractor,
        currencyInteractor,
        budgetInteractor
    )
}
