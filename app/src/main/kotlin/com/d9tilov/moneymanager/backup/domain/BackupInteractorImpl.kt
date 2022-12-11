package com.d9tilov.moneymanager.backup.domain

import com.d9tilov.android.datastore.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow

class BackupInteractorImpl(private val backupRepo: BackupRepo) : BackupInteractor {

    override fun getBackupData(): Flow<com.d9tilov.android.datastore.model.BackupData> = backupRepo.getBackupData()
    override suspend fun makeBackup(): ResultOf<com.d9tilov.android.datastore.model.BackupData> = backupRepo.makeBackup()
    override suspend fun restoreBackup(): ResultOf<Any> = backupRepo.restoreBackup()
    override suspend fun deleteBackup() = backupRepo.deleteBackup()
}
