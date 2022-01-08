package com.d9tilov.moneymanager.transaction.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.exchanger.domain.ExchangeInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.transaction.data.TransactionDataRepo
import com.d9tilov.moneymanager.transaction.data.local.TransactionDao
import com.d9tilov.moneymanager.transaction.data.local.TransactionLocalSource
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractorImpl
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class TransactionModule {

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideTransactionLocalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
    ): TransactionSource = TransactionLocalSource(
        preferencesStore,
        appDatabase.transactionDao()
    )

    @Provides
    fun provideTransactionRepo(transactionSource: TransactionSource): TransactionRepo =
        TransactionDataRepo(transactionSource)

    @Provides
    fun provideTransactionInteractor(
        transactionRepo: TransactionRepo,
        regularTransactionInteractor: RegularTransactionInteractor,
        categoryInteractor: CategoryInteractor,
        userInteractor: UserInteractor,
        currencyInteractor: CurrencyInteractor,
        exchangeInteractor: ExchangeInteractor,
        budgetInteractor: BudgetInteractor
    ): TransactionInteractor = TransactionInteractorImpl(
        transactionRepo,
        regularTransactionInteractor,
        categoryInteractor,
        userInteractor,
        currencyInteractor,
        exchangeInteractor,
        budgetInteractor
    )
}
