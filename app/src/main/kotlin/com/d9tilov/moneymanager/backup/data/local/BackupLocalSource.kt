package com.d9tilov.moneymanager.backup.data.local

import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import kotlinx.coroutines.flow.Flow

class BackupLocalSource(
    private val backupManager: BackupManager,
    private val preferencesStore: PreferencesStore
) : BackupSource {

    override fun getBackupData(): Flow<BackupData> = preferencesStore.backupData
    override suspend fun makeBackup(): ResultOf<BackupData> = backupManager.backupDb()
    override suspend fun restoreBackup(): ResultOf<Any> = backupManager.restoreDb()
}
