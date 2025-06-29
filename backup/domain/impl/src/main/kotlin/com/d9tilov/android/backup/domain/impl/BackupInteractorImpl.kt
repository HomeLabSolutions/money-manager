package com.d9tilov.android.backup.domain.impl

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.contract.BackupRepo
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupInteractorImpl @Inject constructor(
    private val backupRepo: BackupRepo,
) : BackupInteractor {
    override fun getBackupData(): Flow<BackupData> = backupRepo.getBackupData()

    override suspend fun makeBackup(): ResultOf<BackupData> = backupRepo.makeBackup()

    override suspend fun restoreBackup(): ResultOf<Long> = backupRepo.restoreBackup()

    override suspend fun deleteBackup() = backupRepo.deleteBackup()
}
