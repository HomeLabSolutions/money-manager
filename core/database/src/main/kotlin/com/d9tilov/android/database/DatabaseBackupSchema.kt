package com.d9tilov.android.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

internal object DatabaseBackupSchema {
    fun validate(
        source: SQLiteDatabase,
        destination: SupportSQLiteDatabase,
        tables: List<String>,
    ) {
        val sourceQuery: (String) -> Cursor = { source.rawQuery(it, null) }
        val destinationQuery: (String) -> Cursor = { destination.query(it) }
        check(identityHash(sourceQuery) == identityHash(destinationQuery)) { "Backup Room schema does not match" }
        tables.forEach { table ->
            check(tableSchema(sourceQuery, table) == tableSchema(destinationQuery, table)) {
                "Backup table schema does not match: $table"
            }
        }
    }

    private fun identityHash(query: (String) -> Cursor): String =
        query("SELECT identity_hash FROM room_master_table WHERE id = 42").use { cursor ->
            check(cursor.moveToFirst() && !cursor.isNull(0)) { "Backup Room schema identity is missing" }
            cursor.getString(0)
        }

    private fun tableSchema(
        query: (String) -> Cursor,
        table: String,
    ): TableSchema =
        TableSchema(
            // Room permits defaults introduced by migrations when an entity specifies no default.
            columns = rows(query, "PRAGMA table_info(${table.sqlIdentifier()})", setOf("cid", "dflt_value")),
            foreignKeys = foreignKeys(query, table),
            indices = indices(query, table),
        )

    private fun foreignKeys(
        query: (String) -> Cursor,
        table: String,
    ): Set<Set<List<String?>>> =
        query("PRAGMA foreign_key_list(${table.sqlIdentifier()})").use { cursor ->
            val groups = mutableMapOf<Int, MutableSet<List<String?>>>()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                groups.getOrPut(id) { mutableSetOf() }.add(cursor.values(setOf("id")))
            }
            groups.values.toSet()
        }

    private fun indices(
        query: (String) -> Cursor,
        table: String,
    ): Set<IndexSchema> =
        query("PRAGMA index_list(${table.sqlIdentifier()})").use { cursor ->
            buildSet {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    add(
                        IndexSchema(
                            properties = cursor.values(setOf("seq", "name")),
                            columns = rows(query, "PRAGMA index_xinfo(${name.sqlIdentifier()})", setOf("cid")),
                        ),
                    )
                }
            }
        }

    private fun rows(
        query: (String) -> Cursor,
        sql: String,
        excluded: Set<String>,
    ): Set<List<String?>> =
        query(sql).use { cursor ->
            buildSet {
                while (cursor.moveToNext()) add(cursor.values(excluded))
            }
        }

    private fun Cursor.values(excluded: Set<String>): List<String?> =
        columnNames.filterNot { it in excluded }.map { name ->
            val index = getColumnIndexOrThrow(name)
            if (isNull(index)) null else getString(index)
        }

    private data class TableSchema(
        val columns: Set<List<String?>>,
        val foreignKeys: Set<Set<List<String?>>>,
        val indices: Set<IndexSchema>,
    )

    private data class IndexSchema(
        val properties: List<String?>,
        val columns: Set<List<String?>>,
    )
}

internal fun String.sqlIdentifier(): String {
    val quote = '"'
    return "$quote${replace(quote.toString(), "$quote$quote")}$quote"
}
