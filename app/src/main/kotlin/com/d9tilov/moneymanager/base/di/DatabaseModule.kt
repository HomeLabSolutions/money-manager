package com.d9tilov.moneymanager.base.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DATABASE_NAME
import com.d9tilov.moneymanager.encryption.digest
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
class DatabaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val dbPassword =
            digest(FirebaseAuth.getInstance().currentUser?.uid, BuildConfig.SALT)
        val passphrase: ByteArray = SQLiteDatabase.getBytes(dbPassword.toCharArray())
        val factory = SupportFactory(passphrase)
        var builder = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        if (!BuildConfig.DEBUG) builder = builder.openHelperFactory(factory)
        return builder.build()
    }
}
