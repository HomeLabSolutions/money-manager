package com.d9tilov.moneymanager.backup

import android.content.Context
import android.net.Uri
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DATABASE_NAME
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BackupManager(private val context: Context) {

    suspend fun backupDb(uid: String): Result<Nothing> = suspendCoroutine { continuation ->
        if (uid.isEmpty()) {
            continuation.resume(Result.error(WrongUidException()))
        }
        val file = context.getDatabasePath(DATABASE_NAME)
        val parentPath = "$uid/$DATABASE_NAME"
        val fileRef = Firebase.storage.reference.child(parentPath)
        val uploadTask = fileRef.putFile(Uri.fromFile(file))
        uploadTask
            .addOnSuccessListener { continuation.resume(Result.success()) }
            .addOnFailureListener { continuation.resume(Result.error(it)) }
    }

    suspend fun restoreDb(uid: String) = suspendCoroutine<Result<Nothing>> { continuation ->
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
        }.addOnFailureListener {
            continuation.resume(Result.error(it))
        }
    }
}
