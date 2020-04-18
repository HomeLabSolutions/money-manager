package com.d9tilov.moneymanager.data.base.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.d9tilov.moneymanager.data.base.local.db.converters.CurrencyConverter
import com.d9tilov.moneymanager.data.base.local.db.converters.DateConverter
import com.d9tilov.moneymanager.data.user.local.UserDao
import com.d9tilov.moneymanager.data.user.local.entities.UserDbModel

@Database(
    entities = [UserDbModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CurrencyConverter::class,
    DateConverter::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
