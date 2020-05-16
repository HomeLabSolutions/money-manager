package com.d9tilov.moneymanager.backup

import android.net.Uri
import android.os.Build
import android.os.FileUtils
import com.d9tilov.moneymanager.data.base.local.db.AppDatabase.Companion.DATABASE_NAME
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject


class BackupManager @Inject constructor(
    private val databaseFile: File,
    private val preferencesStore: PreferencesStore
) {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    fun backupDatabase() {
        val databaseUri = Uri.fromFile(databaseFile)
        val riversRef: StorageReference =
            storageReference.child("${preferencesStore.uid}/$DATABASE_NAME")

        riversRef.putFile(databaseUri)
            .addOnSuccessListener { taskSnapshot ->
                Timber.d("File was uploaded successfully")
            }
            .addOnFailureListener {
                Timber.d("Error while uploading file: ${it.message}")
            }
    }

    fun restoreDatabase() {
        if (preferencesStore.databaseLoaded) {
            return
        }
        val riversRef: StorageReference =
            storageReference.child("${preferencesStore.uid}/${DATABASE_NAME}")
        val restoredDatabaseFile = File("${DATABASE_NAME}.db")
        riversRef.getFile(restoredDatabaseFile)
            .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                Timber.d("File ${restoredDatabaseFile.name} was downloaded successfully")
                val oldFileOutputStream = FileOutputStream(databaseFile)
                try {
                    val newFileInputStream = FileInputStream(restoredDatabaseFile)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        FileUtils.copy(newFileInputStream, oldFileOutputStream)
                    } else {
                        com.d9tilov.moneymanager.core.util.FileUtils.copyFile(
                            newFileInputStream,
                            oldFileOutputStream
                        )
                    }
                    if (restoredDatabaseFile.exists()) {
                        restoredDatabaseFile.delete()
                    }
                } catch (ex: FileNotFoundException) {
                    Timber.d("File ${preferencesStore.uid}/${DATABASE_NAME} not found in remote storage")
                }
            }).addOnFailureListener(OnFailureListener {
                Timber.d("Error while downloading file: ${it.message}")
            })
        preferencesStore.databaseLoaded = true
    }
}
