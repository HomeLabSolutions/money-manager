package com.d9tilov.moneymanager.backup.domain

import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import kotlinx.coroutines.flow.Flow

class BackupInteractorImpl(private val backupRepo: BackupRepo) : BackupInteractor {

    override suspend fun makeBackup(): ResultOf<BackupData> = backupRepo.makeBackup()

    override suspend fun restoreBackup(): ResultOf<Any> = backupRepo.restoreBackup()
    override fun getBackupData(): Flow<BackupData> = backupRepo.getBackupData()
}
