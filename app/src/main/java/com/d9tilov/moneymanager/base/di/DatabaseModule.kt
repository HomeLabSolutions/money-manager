package com.d9tilov.moneymanager.base.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
class DatabaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideDatabase(
        @ApplicationContext context: Context,
        preferencesStore: PreferencesStore
    ): AppDatabase {
        val uid = runBlocking { preferencesStore.uid.first() }
        val passphrase: ByteArray = SQLiteDatabase.getBytes(uid?.toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(factory)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }
}
