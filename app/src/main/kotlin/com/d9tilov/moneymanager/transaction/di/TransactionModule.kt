package com.d9tilov.moneymanager.transaction.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.transaction.data.TransactionDataRepo
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.moneymanager.transaction.data.local.TransactionLocalSource
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractorImpl
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import com.d9tilov.android.user.domain.contract.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object TransactionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionLocalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): TransactionSource = TransactionLocalSource(
        preferencesStore,
        appDatabase.transactionDao()
    )

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionRepo(transactionSource: TransactionSource): TransactionRepo =
        TransactionDataRepo(transactionSource)

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
