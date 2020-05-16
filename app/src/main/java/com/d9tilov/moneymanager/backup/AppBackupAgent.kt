package com.d9tilov.moneymanager.backup

import android.app.backup.BackupAgentHelper
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.app.backup.BackupManager
import android.app.backup.FileBackupHelper
import android.app.backup.SharedPreferencesBackupHelper
import android.content.Context
import android.os.ParcelFileDescriptor
import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import java.io.IOException

class AppBackupAgent : BackupAgentHelper() {

    override fun onCreate() {
        // Allocate a helper and add it to the backup agent
        SharedPreferencesBackupHelper(this, PreferencesStore.STORE_NAME).also {
            addHelper("prefs", it)
        }
    }

    @Throws(IOException::class)
    override fun onBackup(
        oldState: ParcelFileDescriptor,
        data: BackupDataOutput,
        newState: ParcelFileDescriptor
    ) {
        // Hold the lock while the FileBackupHelper performs backup
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

    companion object {

        fun requestBackup(context: Context) {
            val bm = BackupManager(context)
            bm.dataChanged()
        }
    }
}
