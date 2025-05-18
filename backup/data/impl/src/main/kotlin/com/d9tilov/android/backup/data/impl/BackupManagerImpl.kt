package com.d9tilov.android.backup.data.impl

import android.content.Context
import android.net.Uri
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.common.android.utils.isNetworkConnected
import com.d9tilov.android.core.constants.DataConstants.DATABASE_NAME
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.constants.ExceptionMessageConstants.FILE_NOT_FOUND
import com.d9tilov.android.core.constants.ExceptionMessageConstants.NETWORK_EXCEPTION
import com.d9tilov.android.core.constants.ExceptionMessageConstants.UID_IS_NULL_OR_EMPTY
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.exception.NetworkException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Named

class BackupManagerImpl
    @Inject constructor(
        @Named(DISPATCHER_IO) private val coroutineDispatcher: CoroutineDispatcher,
        private val analyticsSender: AnalyticsSender,
        private val context: Context,
        private val preferencesStore: PreferencesStore,
    ) : BackupManager {
        override suspend fun backupDb(): ResultOf<BackupData> {
            Timber.tag(TAG).d("$BACKUP backupDb before")
            val uid = preferencesStore.uid.firstOrNull()
            if (uid.isNullOrEmpty()) {
                val message = "$BACKUP $UID_IS_NULL_OR_EMPTY: $uid"
                Timber.tag(TAG).e(message)
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to message
                }
                return ResultOf.Failure(WrongUidException())
            }
            if (!isNetworkConnected(context)) {
                val message = "$BACKUP $NETWORK_EXCEPTION"
                Timber.tag(TAG).e(message)
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to message
                }
                return ResultOf.Failure(NetworkException())
            }
            val file = context.getDatabasePath(DATABASE_NAME)
            if (!file.exists()) {
                val message = "$BACKUP $FILE_NOT_FOUND: $DATABASE_NAME"
                Timber.tag(TAG).e(message)
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to message
                }
                return ResultOf.Failure(FileNotFoundException())
            }
            val parentPath = createParentPath(uid)
            return try {
                val fileRef: UploadTask.TaskSnapshot =
                    Firebase.storage.reference
                        .child(parentPath)
                        .putFile(Uri.fromFile(file))
                        .await()
                Timber.tag(TAG).d("Backup was compete successfully")
                analyticsSender.send(AnalyticsEvent.Internal.Backup.Saved)
                ResultOf.Success(
                    BackupData.EMPTY.copy(
                        lastBackupTimestamp = fileRef.metadata?.updatedTimeMillis ?: currentDateTime().toMillis(),
                    ),
                )
            } catch (ex: Exception) {
                Timber.tag(TAG).e("Backup failed: $ex")
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to ex.toString()
                }
                ResultOf.Failure(ex)
            }
        }

        override suspend fun restoreDb(): ResultOf<Long> =
            withContext(coroutineDispatcher) {
                val uid = preferencesStore.uid.firstOrNull()
                if (uid.isNullOrEmpty()) {
                    val message = "$RESTORE $UID_IS_NULL_OR_EMPTY: $uid"
                    Timber.tag(TAG).e(message)
                    analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                        AnalyticsParams.Exception to message
                    }
                    return@withContext ResultOf.Failure(WrongUidException())
                }
                if (!isNetworkConnected(context)) {
                    val message = "$RESTORE $NETWORK_EXCEPTION"
                    Timber.tag(TAG).e(message)
                    analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                        AnalyticsParams.Exception to message
                    }
                    return@withContext ResultOf.Failure(NetworkException())
                }
                val parentPath = createParentPath(uid)
                val fileRef = Firebase.storage.reference.child(parentPath)
                var localFile: File? = null
                try {
                    localFile = File.createTempFile(DATABASE_NAME, "db")
                    fileRef.getFile(localFile).await()
                    val output = context.getDatabasePath(DATABASE_NAME)
                    FileInputStream(localFile.path).use { inputStream ->
                        val buffer = ByteArray(BUFFER_SIZE)
                        var length: Int
                        FileOutputStream(output.path).use { outputStream ->
                            while (inputStream.read(buffer).also { length = it } > 0) {
                                outputStream.write(buffer, 0, length)
                            }
                            outputStream.flush()
                        }
                    }
                    val metadata = fileRef.metadata.await()
                    Timber.tag(TAG).d("Database was restored successfully")
                    analyticsSender.send(AnalyticsEvent.Internal.Backup.Restored)
                    ResultOf.Success(metadata.updatedTimeMillis)
                } catch (ex: Exception) {
                    Timber.tag(TAG).e("Restore backup failed: $ex")
                    analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                        AnalyticsParams.Exception to ex.message
                    }
                    ResultOf.Failure(ex)
                } finally {
                    Timber.tag(TAG).i("Temp file was removed")
                    localFile?.delete()
                }
            }

        override suspend fun deleteBackup(): ResultOf<Unit> {
            val uid = preferencesStore.uid.firstOrNull()
            if (uid.isNullOrEmpty()) {
                val message = "$DELETE $UID_IS_NULL_OR_EMPTY: $uid"
                Timber.tag(TAG).e(message)
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to message
                }
                return ResultOf.Failure(WrongUidException())
            }
            if (!isNetworkConnected(context)) {
                val message = "$DELETE $NETWORK_EXCEPTION"
                Timber.tag(TAG).e(message)
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to message
                }
                return ResultOf.Failure(NetworkException())
            }
            val parentPath = createParentPath(uid)
            return try {
                Firebase.storage.reference
                    .child(parentPath)
                    .delete()
                    .await()
                Timber.tag(TAG).d("Backup deleted successfully")
                analyticsSender.send(AnalyticsEvent.Internal.Backup.Delete)
                ResultOf.Success(Unit)
            } catch (ex: Exception) {
                Timber.tag(TAG).e(ex, "Failed to delete backup")
                analyticsSender.sendWithParams(AnalyticsEvent.Internal.Backup.Error) {
                    AnalyticsParams.Exception to ex.message
                }
                ResultOf.Failure(ex)
            }
        }

        private fun createParentPath(uid: String) = "${uid.normalizePath()}/$DATABASE_NAME"

        private companion object {
            const val BUFFER_SIZE = 1024
            const val BACKUP = "[Backup]"
            const val RESTORE = "[Restore]"
            const val DELETE = "[Delete]"
        }
    }

fun String.normalizePath() = this.replace("/", "")
