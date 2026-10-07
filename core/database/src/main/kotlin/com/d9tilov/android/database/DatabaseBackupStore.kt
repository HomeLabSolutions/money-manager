package com.d9tilov.android.database

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseBackupStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val database: AppDatabase,
) {
    fun restore(
        file: File,
        uid: String,
    ) {
        validateBackup(file, uid)
        val backup = Room.databaseBuilder(context, AppDatabase::class.java, file.absolutePath).build()
        try {
            // Open and validate the downloaded schema before changing any live data.
            val source = backup.openHelper.writableDatabase
            database.runInTransaction {
                val destination = database.openHelper.writableDatabase
                val tables = backupTables()
                destination.execSQL("PRAGMA defer_foreign_keys = ON")
                tables.forEach { table -> destination.execSQL("DELETE FROM ${table.sqlIdentifier()}") }
                tables.forEach { table ->
                    source.query("SELECT * FROM ${table.sqlIdentifier()}").use { cursor ->
                        while (cursor.moveToNext()) {
                            val values = android.content.ContentValues()
                            DatabaseUtils.cursorRowToContentValues(cursor, values)
                            destination.insert(table.sqlIdentifier(), SQLiteDatabase.CONFLICT_ABORT, values)
                        }
                    }
                }
            }
        } finally {
            backup.close()
        }
    }

    fun snapshot(file: File) {
        // The app uses TRUNCATE journaling. Hold a write transaction so the main
        // database file cannot change while it is copied for upload.
        database.runInTransaction {
            val source = database.openHelper.writableDatabase
            check(!source.isWriteAheadLoggingEnabled) { "A file snapshot requires rollback journaling" }
            File(source.path).copyTo(file, overwrite = true)
        }
    }

    private fun validateBackup(
        file: File,
        uid: String,
    ) {
        SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READONLY).use { backup ->
            check(backup.version == AppDatabase.VERSION_NUMBER) { "Backup database version is not supported" }
            check(backup.isDatabaseIntegrityOk) { "Backup database is corrupted" }
            backup.rawQuery("PRAGMA foreign_key_check", null).use { cursor ->
                check(!cursor.moveToFirst()) { "Backup database contains broken references" }
            }
            backup.rawQuery("SELECT uid FROM users WHERE uid = ?", arrayOf(uid)).use { cursor ->
                check(cursor.moveToFirst()) { "Backup does not contain the current user" }
            }
        }
    }

    private fun backupTables(): List<String> =
        database.openHelper.writableDatabase
            .query(
                """
                SELECT name FROM sqlite_master
                WHERE type = 'table'
                  AND name NOT GLOB 'sqlite_*'
                  AND name NOT IN ('android_metadata', 'room_master_table')
                ORDER BY name
                """.trimIndent(),
            ).use { cursor ->
                buildList {
                    while (cursor.moveToNext()) add(cursor.getString(0))
                }
            }

    private fun String.sqlIdentifier(): String {
        val quote = '"'
        return "$quote${replace(quote.toString(), "$quote$quote")}$quote"
    }
}
