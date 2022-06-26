package com.d9tilov.moneymanager.backup.domain

import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.domain.Interactor
import kotlinx.coroutines.flow.Flow

interface BackupInteractor : Interactor {

    fun getBackupData(): Flow<BackupData>
    suspend fun makeBackup(): ResultOf<BackupData>
    suspend fun restoreBackup(): ResultOf<Any>
    suspend fun deleteBackup(): ResultOf<Any>
}
