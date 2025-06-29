package com.d9tilov.android.backup.data.contract

import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.model.ResultOf

interface BackupManager {
    suspend fun backupDb(): ResultOf<BackupData>

    suspend fun restoreDb(): ResultOf<Long>

    suspend fun deleteBackup(): ResultOf<Unit>
}
