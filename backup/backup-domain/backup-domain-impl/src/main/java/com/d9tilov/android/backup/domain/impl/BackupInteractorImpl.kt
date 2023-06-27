package com.d9tilov.android.backup.domain.impl

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.contract.BackupRepo
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow

class BackupInteractorImpl(private val backupRepo: BackupRepo) : BackupInteractor {

    override fun getBackupData(): Flow<BackupData> = backupRepo.getBackupData()
    override suspend fun makeBackup(): ResultOf<BackupData> = backupRepo.makeBackup()
    override suspend fun restoreBackup(): ResultOf<Any> = backupRepo.restoreBackup()
    override suspend fun deleteBackup() = backupRepo.deleteBackup()
}
