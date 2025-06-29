package com.d9tilov.android.database.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.dao.BudgetDao
import com.d9tilov.android.database.dao.CategoryDao
import com.d9tilov.android.database.dao.CurrencyListDao
import com.d9tilov.android.database.dao.GoalDao
import com.d9tilov.android.database.dao.MainCurrencyDao
import com.d9tilov.android.database.dao.RegularTransactionDao
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun providesCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun providesTransactionDao(database: AppDatabase): TransactionDao = database.transactionDao()

    @Provides
    fun providesBudgetDao(database: AppDatabase): BudgetDao = database.budgetDao()

    @Provides
    fun providesCurrencyListDao(database: AppDatabase): CurrencyListDao = database.currencyDao()

    @Provides
    fun providesMainCurrencyDao(database: AppDatabase): MainCurrencyDao = database.mainCurrencyDao()

    @Provides
    fun providesRegularTransactionDao(database: AppDatabase): RegularTransactionDao = database.regularTransactionDao()

    @Provides
    fun providesGoalDao(database: AppDatabase): GoalDao = database.goalDao()
}
