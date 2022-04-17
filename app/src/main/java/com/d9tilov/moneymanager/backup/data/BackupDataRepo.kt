package com.d9tilov.moneymanager.backup.data

import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.backup.data.local.BackupSource
import com.d9tilov.moneymanager.backup.domain.BackupRepo
import com.d9tilov.moneymanager.base.data.ResultOf
import kotlinx.coroutines.flow.Flow

class BackupDataRepo(private val backupSource: BackupSource) : BackupRepo {

    override fun getBackupData(): Flow<BackupData> = backupSource.getBackupData()
    override suspend fun makeBackup(): ResultOf<BackupData> = backupSource.makeBackup()
    override suspend fun restoreBackup(): ResultOf<Any> = backupSource.restoreBackup()
}
