package com.d9tilov.android.backup.data.impl

import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.exception.NetworkException
import com.google.firebase.FirebaseException
import java.io.FileNotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BackupLocalSource(
    private val backupManager: BackupManager,
    private val preferencesStore: PreferencesStore
) : BackupSource {

    override fun getBackupData(): Flow<BackupData> = preferencesStore.backupData.map { time -> BackupData(time) }
    override suspend fun makeBackup(): ResultOf<BackupData> = backupManager.backupDb()
    override suspend fun restoreBackup(): ResultOf<Any> = backupManager.restoreDb()
    override suspend fun deleteBackup(): ResultOf<Any> {
        return try {
            backupManager.deleteBackup()
            preferencesStore.updateLastBackupDate(-1L)
            ResultOf.Success(Any())
        } catch (ex: NetworkException) {
            ResultOf.Failure(ex)
        } catch (ex: WrongUidException) {
            ResultOf.Failure(ex)
        } catch (ex: FileNotFoundException) {
            ResultOf.Failure(ex)
        } catch (ex: FirebaseException) {
            ResultOf.Failure(ex)
        }
    }
}
