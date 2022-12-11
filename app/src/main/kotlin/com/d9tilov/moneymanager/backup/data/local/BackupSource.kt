package com.d9tilov.moneymanager.backup.data.local

import com.d9tilov.android.datastore.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow

interface BackupSource {

    fun getBackupData(): Flow<com.d9tilov.android.datastore.model.BackupData>
    suspend fun makeBackup(): ResultOf<com.d9tilov.android.datastore.model.BackupData>
    suspend fun restoreBackup(): ResultOf<Any>
    suspend fun deleteBackup(): ResultOf<Any>
}
