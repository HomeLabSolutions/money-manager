package com.d9tilov.moneymanager.backup.data.local

import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.datastore.PreferencesStore.Companion.UNKNOWN_BACKUP_DATE
import com.d9tilov.android.datastore.model.BackupData
import com.google.firebase.FirebaseException
import kotlinx.coroutines.flow.Flow
import java.io.FileNotFoundException

class BackupLocalSource(
    private val backupManager: BackupManager,
    private val preferencesStore: PreferencesStore
) : BackupSource {

    override fun getBackupData(): Flow<BackupData> = preferencesStore.backupData
    override suspend fun makeBackup(): ResultOf<BackupData> = backupManager.backupDb()
    override suspend fun restoreBackup(): ResultOf<Any> = backupManager.restoreDb()
    override suspend fun deleteBackup(): ResultOf<Any> {
        return try {
            backupManager.deleteBackup()
            preferencesStore.updateLastBackupDate(UNKNOWN_BACKUP_DATE)
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
