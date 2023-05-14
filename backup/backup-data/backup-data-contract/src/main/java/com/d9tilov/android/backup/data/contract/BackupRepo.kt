package com.d9tilov.android.backup.data.contract

import com.d9tilov.android.backup.data.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow

interface BackupRepo {

    fun getBackupData(): Flow<BackupData>
    suspend fun makeBackup(): ResultOf<BackupData>
    suspend fun restoreBackup(): ResultOf<Any>
    suspend fun deleteBackup(): ResultOf<Any>
}
