package com.d9tilov.android.backup.domain.contract

import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow

interface BackupInteractor {
    fun getBackupData(): Flow<BackupData>

    suspend fun makeBackup(): ResultOf<BackupData>

    suspend fun restoreBackup(): ResultOf<Any>

    suspend fun deleteBackup(): ResultOf<Any>
}
