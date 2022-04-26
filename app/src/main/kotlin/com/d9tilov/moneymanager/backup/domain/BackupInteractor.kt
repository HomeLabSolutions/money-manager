package com.d9tilov.moneymanager.backup.domain

import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.domain.Interactor
import kotlinx.coroutines.flow.Flow

interface BackupInteractor : Interactor {

    suspend fun makeBackup(): ResultOf<BackupData>
    suspend fun restoreBackup(): ResultOf<Any>
    fun getBackupData(): Flow<BackupData>
}
