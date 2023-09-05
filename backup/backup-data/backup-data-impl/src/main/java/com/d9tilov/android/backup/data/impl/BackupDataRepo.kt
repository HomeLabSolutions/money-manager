package com.d9tilov.android.backup.data.impl

import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.domain.contract.BackupRepo
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupDataRepo @Inject constructor(private val backupSource: BackupSource) : BackupRepo {

    override fun getBackupData(): Flow<BackupData> = backupSource.getBackupData()
    override suspend fun makeBackup(): ResultOf<BackupData> = backupSource.makeBackup()
    override suspend fun restoreBackup(): ResultOf<Any> = backupSource.restoreBackup()
    override suspend fun deleteBackup() = backupSource.deleteBackup()
}
