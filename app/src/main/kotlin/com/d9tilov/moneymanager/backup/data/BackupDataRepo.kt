package com.d9tilov.moneymanager.backup.data

import com.d9tilov.android.datastore.model.BackupData
import com.d9tilov.moneymanager.backup.data.local.BackupSource
import com.d9tilov.moneymanager.backup.domain.BackupRepo
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow

class BackupDataRepo(private val backupSource: BackupSource) : BackupRepo {

    override fun getBackupData(): Flow<com.d9tilov.android.datastore.model.BackupData> = backupSource.getBackupData()
    override suspend fun makeBackup(): ResultOf<com.d9tilov.android.datastore.model.BackupData> = backupSource.makeBackup()
    override suspend fun restoreBackup(): ResultOf<Any> = backupSource.restoreBackup()
    override suspend fun deleteBackup() = backupSource.deleteBackup()
}
