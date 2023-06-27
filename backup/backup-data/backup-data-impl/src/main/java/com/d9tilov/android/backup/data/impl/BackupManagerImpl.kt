package com.d9tilov.android.backup.data.impl

import android.content.Context
import android.net.Uri
import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.common_android.utils.isNetworkConnected
import com.d9tilov.android.core.constants.DataConstants.DATABASE_NAME
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.exception.NetworkException
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BackupManagerImpl(
    private val context: Context,
    private val preferencesStore: PreferencesStore
) : BackupManager {

    override suspend fun backupDb(): ResultOf<BackupData> {
        Timber.tag(TAG).d("Backup backupDb before")
        val uid = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
        Timber.tag(TAG).d("Backup backupDb: $uid")
        return suspendCancellableCoroutine { continuation ->
            if (!isNetworkConnected(context)) {
                continuation.resumeWithException(NetworkException())
                return@suspendCancellableCoroutine
            }
            Timber.tag(TAG).d("Backup start")
            if (uid.isNullOrEmpty()) {
                Timber.tag(TAG).d("Empty uid")
                continuation.resumeWithException(WrongUidException())
                return@suspendCancellableCoroutine
            }
            val file = context.getDatabasePath(DATABASE_NAME)
            if (!file.exists()) {
                continuation.resumeWithException(FileNotFoundException())
                return@suspendCancellableCoroutine
            }
            val parentPath = "${uid.normalizePath()}/$DATABASE_NAME"
            val fileRef = Firebase.storage.reference.child(parentPath)
            val uploadTask = fileRef.putFile(Uri.fromFile(file))
            Timber.tag(TAG).d("Backup end")
            uploadTask
                .addOnSuccessListener {
                    if (!continuation.isActive) return@addOnSuccessListener
                    val backupDate = currentDateTime().toMillis()
                    runBlocking { preferencesStore.updateLastBackupDate(backupDate) }
                    continuation.resume(ResultOf.Success(BackupData.EMPTY.copy(lastBackupTimestamp = backupDate)))
                    Timber.tag(TAG).d("Backup was compete successfully")
                }
                .addOnFailureListener {
                    if (!continuation.isActive) return@addOnFailureListener
                    continuation.resumeWithException(FirebaseException("Make backup", it))
                    Timber.tag(TAG).d("Backup was compete with error: $it")
                }
        }
    }

    override suspend fun restoreDb(): ResultOf<Any> {
        val uid = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
        return suspendCancellableCoroutine { continuation ->
            if (!isNetworkConnected(context)) continuation.resumeWithException(NetworkException())
            if (uid.isNullOrEmpty()) continuation.resumeWithException(WrongUidException())
            val parentPath = "${uid?.normalizePath()}/$DATABASE_NAME"
            val fileRef = Firebase.storage.reference.child(parentPath)
            val localFile = File.createTempFile(DATABASE_NAME, "db")
            fileRef.getFile(localFile).addOnSuccessListener {
                if (!continuation.isActive) return@addOnSuccessListener
                val output = context.getDatabasePath(DATABASE_NAME)
                val myInputs: InputStream = FileInputStream(localFile.path)
                val buffer = ByteArray(BUFFER_SIZE)
                var length: Int
                val myOutput = FileOutputStream(output.path)
                while (myInputs.read(buffer).also { length = it } > 0) {
                    myOutput.write(buffer, 0, length)
                }
                myOutput.flush()
                myOutput.close()
                myInputs.close()
                fileRef.metadata.addOnSuccessListener { metadata ->
                    runBlocking { preferencesStore.updateLastBackupDate(metadata.updatedTimeMillis) }
                }
                continuation.resume(ResultOf.Success(Any()))
                Timber.tag(TAG).d("Restore was compete successfully")
            }.addOnFailureListener {
                if (!continuation.isActive) return@addOnFailureListener
                continuation.resumeWithException(FirebaseException("Restore backup", it))
                Timber.tag(TAG).d("Restore was complete with error: $it")
            }
        }
    }

    override suspend fun deleteBackup(): ResultOf<Any> {
        val uid = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return suspendCancellableCoroutine { continuation ->
            if (!isNetworkConnected(context)) {
                continuation.resumeWithException(NetworkException())
                return@suspendCancellableCoroutine
            }
            if (uid.isNullOrEmpty()) {
                continuation.resumeWithException(WrongUidException())
                return@suspendCancellableCoroutine
            }
            val file = context.getDatabasePath(DATABASE_NAME)
            if (!file.exists()) {
                continuation.resumeWithException(FileNotFoundException())
                return@suspendCancellableCoroutine
            }
            val parentPath = "${uid.normalizePath()}/$DATABASE_NAME"
            val fileRef = Firebase.storage.reference.child(parentPath)
            val deleteTask = fileRef.delete()
            deleteTask.addOnSuccessListener {
                if (!continuation.isActive) return@addOnSuccessListener
                continuation.resume(ResultOf.Success(Any()))
                Timber.tag(TAG).d("Deleting Firebase file was compete successfully")
            }.addOnFailureListener {
                if (!continuation.isActive) return@addOnFailureListener
                continuation.resumeWithException(FirebaseException("Delete backup", it))
                Timber.tag(TAG).d("Deleting Firebase file was complete with error: $it")
            }
        }
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }
}

fun String.normalizePath() = this.replace("/", "")
