package com.d9tilov.moneymanager.base.di

import android.content.Context
import androidx.room.Room
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }
}
