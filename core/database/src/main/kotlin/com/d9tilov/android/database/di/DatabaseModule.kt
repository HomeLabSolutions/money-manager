package com.d9tilov.android.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d9tilov.android.core.constants.DataConstants.DATABASE_NAME
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.migration.MIGRATION_1_2
import com.d9tilov.android.database.migration.MIGRATION_2_3
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun provideDatabase(context: Context): AppDatabase =
    Room
        .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .build()

val databaseModule =
    module {
        single { create(::provideDatabase) }
    }
