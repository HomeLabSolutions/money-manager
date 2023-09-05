package com.d9tilov.android.backup.data.impl

import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import com.d9tilov.android.network.exception.NetworkException
import com.google.firebase.FirebaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import javax.inject.Inject

class BackupLocalSource @Inject constructor(
    @Dispatcher(MoneyManagerDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val backupManager: BackupManager,
    private val preferencesStore: PreferencesStore
) : BackupSource {

    override fun getBackupData(): Flow<BackupData> = preferencesStore.backupData
        .map { time -> BackupData(time) }
        .flowOn(dispatcher)

    override suspend fun makeBackup(): ResultOf<BackupData> =
        withContext(dispatcher) { backupManager.backupDb() }

    override suspend fun restoreBackup(): ResultOf<Any> =
        withContext(dispatcher) { backupManager.restoreDb() }

    override suspend fun deleteBackup(): ResultOf<Any> = withContext(dispatcher) {
        try {
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
