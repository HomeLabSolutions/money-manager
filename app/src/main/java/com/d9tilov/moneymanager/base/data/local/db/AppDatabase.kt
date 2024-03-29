package com.d9tilov.moneymanager.base.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.d9tilov.moneymanager.base.data.local.db.converters.CurrencyConverter
import com.d9tilov.moneymanager.base.data.local.db.converters.DateConverter
import com.d9tilov.moneymanager.base.data.local.db.converters.ModelTypeConverter
import com.d9tilov.moneymanager.category.data.local.CategoryDao
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import com.d9tilov.moneymanager.user.data.local.UserDao
import com.d9tilov.moneymanager.user.data.local.entities.UserDbModel

@Database(
    entities = [
        UserDbModel::class,
        CategoryDbModel::class
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

    companion object {

        const val DATABASE_NAME = "money-manager-db"
        const val DEFAULT_DATA_ID = 0L
        const val NO_ID = -1L

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
