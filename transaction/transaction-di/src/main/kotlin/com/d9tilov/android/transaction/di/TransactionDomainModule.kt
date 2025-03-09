package com.d9tilov.android.transaction.di

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.impl.TransactionInteractorImpl
import com.d9tilov.android.user.domain.contract.UserInteractor
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
        budgetInteractor: BudgetInteractor,
    ): TransactionInteractor =
        TransactionInteractorImpl(
            transactionRepo,
            regularTransactionInteractor,
            categoryInteractor,
            userInteractor,
            currencyInteractor,
            budgetInteractor,
        )
}
