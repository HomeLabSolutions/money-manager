package com.d9tilov.moneymanager.base.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.d9tilov.moneymanager.base.data.local.db.converters.CurrencyConverter
import com.d9tilov.moneymanager.base.data.local.db.converters.DateConverter
import com.d9tilov.moneymanager.base.data.local.db.converters.ModelTypeConverter
import com.d9tilov.moneymanager.category.data.local.CategoryDao
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import com.d9tilov.moneymanager.transaction.data.local.TransactionDao
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import com.d9tilov.moneymanager.user.data.local.UserDao
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel

@Database(
    entities = [
        UserDbModel::class,
        CategoryDbModel::class,
        TransactionDbModel::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CurrencyConverter::class,
    DateConverter::class,
    ModelTypeConverter::class
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
