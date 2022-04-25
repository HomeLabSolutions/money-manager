package com.d9tilov.moneymanager.backup

import android.content.Context
import android.net.Uri
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DATABASE_NAME
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.toMillis
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BackupManager(private val context: Context, private val preferencesStore: PreferencesStore) {

    suspend fun backupDb(): ResultOf<BackupData> {
        if (!isNetworkConnected(context)) return ResultOf.Failure(NetworkException())
        val uid = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return suspendCoroutine { continuation ->
            Timber.tag(App.TAG).d("Backup start")
            if (uid.isNullOrEmpty()) {
                Timber.tag(App.TAG).d("Empty uid")
                return@suspendCoroutine continuation.resume(ResultOf.Failure(WrongUidException()))
            }
            val file = context.getDatabasePath(DATABASE_NAME)
            if (!file.exists()) {
                return@suspendCoroutine continuation.resume(ResultOf.Failure(FileNotFoundException()))
            }
            val parentPath = "${uid.normalizePath()}/$DATABASE_NAME"
            val fileRef = Firebase.storage.reference.child(parentPath)
            val uploadTask = fileRef.putFile(Uri.fromFile(file))
            Timber.tag(App.TAG).d("Backup end")
            uploadTask
                .addOnSuccessListener {
                    val backupDate = currentDateTime().toMillis()
                    runBlocking { preferencesStore.updateLastBackupDate(backupDate) }
                    continuation.resume(ResultOf.Success(BackupData.EMPTY.copy(lastBackupTimestamp = backupDate)))
                    Timber.tag(App.TAG).d("Backup was compete successfully")
                }
                .addOnFailureListener {
                    continuation.resume(ResultOf.Failure(it))
                    Timber.tag(App.TAG).d("Backup was compete with error: $it")
                }
        }
    }

    suspend fun restoreDb(): ResultOf<Any> {
        val uid = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return suspendCoroutine { continuation ->
            if (uid.isNullOrEmpty()) {
                return@suspendCoroutine continuation.resume(ResultOf.Failure(WrongUidException()))
            }
            val parentPath = "${uid.normalizePath()}/$DATABASE_NAME"
            val fileRef: StorageReference = Firebase.storage.reference.child(parentPath)
            val localFile = File.createTempFile(DATABASE_NAME, "db")
            fileRef.getFile(localFile).addOnSuccessListener {
                val output = context.getDatabasePath(DATABASE_NAME)
                val myInputs: InputStream = FileInputStream(localFile.path)
                val buffer = ByteArray(1024)
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
                continuation.resume(ResultOf.Failure(it))
                Timber.tag(App.TAG).d("Restore was complete with error: $it")
            }
        }
    }
}

private fun String.normalizePath() = this.trim()
