package com.d9tilov.moneymanager.backup

import android.content.Context
import android.net.Uri
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DATABASE_NAME
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BackupManager (private val context: Context, private val preferencesStore: PreferencesStore) {

    suspend fun backupDb(): Result<Nothing> = suspendCoroutine { continuation ->
        Timber.tag(App.TAG).d("Backup start")
        val uid = preferencesStore.uid
        if (uid.isNullOrEmpty()) {
            Timber.tag(App.TAG).d("Empty uid")
            continuation.resume(Result.error(WrongUidException()))
        }
        val file = context.getDatabasePath(DATABASE_NAME)
        val parentPath = "$uid/$DATABASE_NAME"
        val fileRef = Firebase.storage.reference.child(parentPath)
        val uploadTask = fileRef.putFile(Uri.fromFile(file))
        Timber.tag(App.TAG).d("Backup end")
        uploadTask
            .addOnSuccessListener {
                continuation.resume(Result.success())
                Timber.tag(App.TAG).d("Backup was compete successfully")
            }
            .addOnFailureListener {
                continuation.resume(Result.error(it))
                Timber.tag(App.TAG).d("Backup was compete with error: $it")
            }
    }

    suspend fun restoreDb() = suspendCoroutine<Result<Nothing>> { continuation ->
        val uid = preferencesStore.uid
        if (uid.isNullOrEmpty()) {
            return@suspendCoroutine continuation.resume(Result.error(WrongUidException()))
        }
        if (uid.isEmpty()) {
            continuation.resume(Result.error(WrongUidException()))
        }
        val parentPath = "$uid/$DATABASE_NAME"
        val fileRef = Firebase.storage.reference.child(parentPath)
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
            continuation.resume(Result.success())
            Timber.tag(App.TAG).d("Restore was compete successfully")
        }.addOnFailureListener {
            continuation.resume(Result.error(it))
            Timber.tag(App.TAG).d("Restore was complete with error: $it")
        }
    }
}
