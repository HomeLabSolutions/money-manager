package com.d9tilov.android.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d9tilov.android.core.constants.DataConstants.DATABASE_NAME
import com.d9tilov.android.core.di.ApplicationContext
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.migration.MIGRATION_1_2
import com.d9tilov.android.database.migration.MIGRATION_2_3
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object DatabaseModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
}
