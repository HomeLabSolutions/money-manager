package com.d9tilov.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.d9tilov.android.database.AppDatabase.Companion.VERSION_NUMBER
import com.d9tilov.android.database.converters.CurrencyConverter
import com.d9tilov.android.database.converters.DateConverter
import com.d9tilov.android.database.converters.ModelTypeConverter
import com.d9tilov.android.database.dao.BudgetDao
import com.d9tilov.android.database.dao.CategoryDao
import com.d9tilov.android.database.dao.CurrencyListDao
import com.d9tilov.android.database.dao.GoalDao
import com.d9tilov.android.database.dao.MainCurrencyDao
import com.d9tilov.android.database.dao.RegularTransactionDao
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.database.dao.UserDao
import com.d9tilov.android.database.entity.BudgetDbModel
import com.d9tilov.android.database.entity.CategoryDbModel
import com.d9tilov.android.database.entity.CurrencyDbModel
import com.d9tilov.android.database.entity.GoalDbModel
import com.d9tilov.android.database.entity.MainCurrencyDbModel
import com.d9tilov.android.database.entity.RegularTransactionDbModel
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.database.entity.UserDbModel

@Database(
    entities = [
        UserDbModel::class,
        CategoryDbModel::class,
        TransactionDbModel::class,
        BudgetDbModel::class,
        CurrencyDbModel::class,
        MainCurrencyDbModel::class,
        RegularTransactionDbModel::class,
        GoalDbModel::class,
    ],
    version = VERSION_NUMBER,
    exportSchema = true,
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

    abstract fun currencyDao(): CurrencyListDao

    abstract fun mainCurrencyDao(): MainCurrencyDao

    abstract fun regularTransactionDao(): RegularTransactionDao

    abstract fun goalDao(): GoalDao

    companion object {
        const val VERSION_NUMBER = 1
    }
}
