package com.d9tilov.moneymanager.backup.data.local

import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import kotlinx.coroutines.flow.Flow

interface BackupSource {

    suspend fun makeBackup(): ResultOf<BackupData>
    suspend fun restoreBackup(): ResultOf<Any>
    fun getBackupData(): Flow<BackupData>
}
