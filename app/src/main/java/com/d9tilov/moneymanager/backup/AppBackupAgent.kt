package com.d9tilov.moneymanager.backup

import android.app.backup.BackupAgentHelper
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.app.backup.FileBackupHelper
import android.app.backup.SharedPreferencesBackupHelper
import android.os.ParcelFileDescriptor
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.DATABASE_NAME
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import java.io.IOException

class AppBackupAgent : BackupAgentHelper() {

    override fun onCreate() {
        SharedPreferencesBackupHelper(this, PreferencesStore.STORE_NAME).also {
            addHelper("prefs", it)
        }
        val fileBackupHelper = FileBackupHelper(this, "../databases/$DATABASE_NAME")
        addHelper(DATABASE_NAME, fileBackupHelper)
    }

    @Throws(IOException::class)
    override fun onBackup(
        oldState: ParcelFileDescriptor,
        data: BackupDataOutput,
        newState: ParcelFileDescriptor
    ) {
        synchronized(this) {
            super.onBackup(oldState, data, newState)
        }
    }

    @Throws(IOException::class)
    override fun onRestore(
        data: BackupDataInput,
        appVersionCode: Int,
        newState: ParcelFileDescriptor
    ) {
        // Hold the lock while the FileBackupHelper restores the file
        synchronized(this) {
            super.onRestore(data, appVersionCode, newState)
        }
    }
}
