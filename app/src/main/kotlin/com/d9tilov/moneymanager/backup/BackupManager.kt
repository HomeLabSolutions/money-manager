package com.d9tilov.moneymanager.backup

import android.content.Context
import android.net.Uri
import com.d9tilov.android.core.constants.DataConstants.DATABASE_NAME
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.toMillis
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BackupManager(private val context: Context, private val preferencesStore: PreferencesStore) {

    suspend fun backupDb(): ResultOf<BackupData> {
        Timber.tag(App.TAG).d("Backup backupDb before")
        val uid = withContext(Dispatchers.IO) { preferencesStore.uid.firstOrNull() }
        Timber.tag(App.TAG).d("Backup backupDb: $uid")
        return suspendCancellableCoroutine { continuation ->
            if (!isNetworkConnected(context)) {
                continuation.resumeWithException(NetworkException())
                return@suspendCancellableCoroutine
            }
            Timber.tag(App.TAG).d("Backup start")
            if (uid.isNullOrEmpty()) {
                Timber.tag(App.TAG).d("Empty uid")
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
            Timber.tag(App.TAG).d("Backup end")
            uploadTask
                .addOnSuccessListener {
                    if (!continuation.isActive) return@addOnSuccessListener
                    val backupDate = currentDateTime().toMillis()
                    runBlocking { preferencesStore.updateLastBackupDate(backupDate) }
                    continuation.resume(ResultOf.Success(BackupData.EMPTY.copy(lastBackupTimestamp = backupDate)))
                    Timber.tag(App.TAG).d("Backup was compete successfully")
                }
                .addOnFailureListener {
                    if (!continuation.isActive) return@addOnFailureListener
                    continuation.resumeWithException(FirebaseException("Make backup", it))
                    Timber.tag(App.TAG).d("Backup was compete with error: $it")
                }
        }
    }

    suspend fun restoreDb(): ResultOf<Any> {
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
                Timber.tag(App.TAG).d("Restore was compete successfully")
            }.addOnFailureListener {
                if (!continuation.isActive) return@addOnFailureListener
                continuation.resumeWithException(FirebaseException("Restore backup", it))
                Timber.tag(App.TAG).d("Restore was complete with error: $it")
            }
        }
    }

    suspend fun deleteBackup(): ResultOf<Any> {
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
                Timber.tag(App.TAG).d("Deleting Firebase file was compete successfully")
            }.addOnFailureListener {
                if (!continuation.isActive) return@addOnFailureListener
                continuation.resumeWithException(FirebaseException("Delete backup", it))
                Timber.tag(App.TAG).d("Deleting Firebase file was complete with error: $it")
            }
        }
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }
}

fun String.normalizePath() = this.replace("/", "")
