package com.d9tilov.android.database.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.dao.BudgetDao
import com.d9tilov.android.database.dao.CategoryDao
import com.d9tilov.android.database.dao.CurrencyListDao
import com.d9tilov.android.database.dao.GoalDao
import com.d9tilov.android.database.dao.InsightDao
import com.d9tilov.android.database.dao.MainCurrencyDao
import com.d9tilov.android.database.dao.RegularTransactionDao
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.database.dao.UserDao
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.factory

fun providesUserDao(database: AppDatabase): UserDao = database.userDao()

fun providesCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

fun providesTransactionDao(database: AppDatabase): TransactionDao = database.transactionDao()

fun providesBudgetDao(database: AppDatabase): BudgetDao = database.budgetDao()

fun providesCurrencyListDao(database: AppDatabase): CurrencyListDao = database.currencyDao()

fun providesMainCurrencyDao(database: AppDatabase): MainCurrencyDao = database.mainCurrencyDao()

fun providesRegularTransactionDao(database: AppDatabase): RegularTransactionDao = database.regularTransactionDao()

fun providesGoalDao(database: AppDatabase): GoalDao = database.goalDao()

fun providesInsightDao(database: AppDatabase): InsightDao = database.insightDao()

val daosModule =
    module {
        factory { create(::providesUserDao) }
        factory { create(::providesCategoryDao) }
        factory { create(::providesTransactionDao) }
        factory { create(::providesBudgetDao) }
        factory { create(::providesCurrencyListDao) }
        factory { create(::providesMainCurrencyDao) }
        factory { create(::providesRegularTransactionDao) }
        factory { create(::providesGoalDao) }
        factory { create(::providesInsightDao) }
    }
