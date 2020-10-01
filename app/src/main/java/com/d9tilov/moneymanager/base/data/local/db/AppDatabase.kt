package com.d9tilov.moneymanager.base.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.d9tilov.moneymanager.base.data.local.db.converters.CurrencyConverter
import com.d9tilov.moneymanager.base.data.local.db.converters.DateConverter
import com.d9tilov.moneymanager.base.data.local.db.converters.ModelTypeConverter
import com.d9tilov.moneymanager.budget.data.local.BudgetDao
import com.d9tilov.moneymanager.budget.data.local.entity.BudgetDbModel
import com.d9tilov.moneymanager.category.data.local.CategoryDao
import com.d9tilov.moneymanager.category.data.local.entity.CategoryDbModel
import com.d9tilov.moneymanager.currency.data.local.CurrencyDao
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyDbModel
import com.d9tilov.moneymanager.fixed.data.local.FixedTransactionDao
import com.d9tilov.moneymanager.fixed.data.local.entity.FixedTransactionDbModel
import com.d9tilov.moneymanager.goal.data.local.GoalDao
import com.d9tilov.moneymanager.goal.data.local.entity.GoalDbModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionDao
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import com.d9tilov.moneymanager.user.data.local.UserDao
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel

@Database(
    entities = [
        UserDbModel::class,
        CategoryDbModel::class,
        TransactionDbModel::class,
        BudgetDbModel::class,
        CurrencyDbModel::class,
        FixedTransactionDbModel::class,
        GoalDbModel::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CurrencyConverter::class,
    DateConverter::class,
    ModelTypeConverter::class,
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun fixedDao(): FixedTransactionDao
    abstract fun goalDao(): GoalDao
}
