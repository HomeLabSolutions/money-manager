package com.d9tilov.android.backup.data.impl

import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.datastore.PreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BackupLocalSource
    @Inject
    constructor(
        private val backupManager: BackupManager,
        private val preferencesStore: PreferencesStore,
    ) : BackupSource {
        override fun getBackupData(): Flow<BackupData> =
            preferencesStore.backupData
                .map { time -> BackupData(time) }

        override suspend fun makeBackup(): ResultOf<BackupData> {
            val result = backupManager.backupDb()
            if (result is ResultOf.Success) preferencesStore.updateLastBackupDate(result.data.lastBackupTimestamp)
            return result
        }

        override suspend fun restoreBackup(): ResultOf<Long> {
            val result: ResultOf<Long> = backupManager.restoreDb()
            if (result is ResultOf.Success) preferencesStore.updateLastBackupDate(result.data)
            return result
        }

        override suspend fun deleteBackup(): ResultOf<Any> {
            val result = backupManager.deleteBackup()
            preferencesStore.updateLastBackupDate(-1L)
            return result
        }
    }
