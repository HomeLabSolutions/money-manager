package com.d9tilov.android.database

import android.database.Cursor

internal object DatabaseBackupSchemaValidator {
    fun validate(
        sourceQuery: (String) -> Cursor,
        destinationQuery: (String) -> Cursor,
        tables: List<String>,
    ) {
        tables.forEach { table ->
            val sourceColumns = columns(sourceQuery, table)
            val destinationColumns = columns(destinationQuery, table)
            check(sourceColumns.isNotEmpty() && destinationColumns.isNotEmpty()) {
                "Backup table is missing: $table"
            }
            val compatibleTypes = sourceColumns.all { (name, column) -> destinationColumns[name]?.type == column.type }
            val requiredColumnsPresent =
                destinationColumns.all { (name, column) -> !column.required || name in sourceColumns }
            check(compatibleTypes && requiredColumnsPresent) {
                "Backup table columns are not compatible: $table"
            }
        }
    }

    private fun columns(
        query: (String) -> Cursor,
        table: String,
    ): Map<String, Column> =
        query("PRAGMA table_info(${table.sqlIdentifier()})").use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow("name")
            val typeIndex = cursor.getColumnIndexOrThrow("type")
            val notNullIndex = cursor.getColumnIndexOrThrow("notnull")
            val defaultIndex = cursor.getColumnIndexOrThrow("dflt_value")
            val primaryKeyIndex = cursor.getColumnIndexOrThrow("pk")
            buildMap {
                while (cursor.moveToNext()) {
                    val requiresValue = cursor.getInt(notNullIndex) != 0 && cursor.isNull(defaultIndex)
                    val required = cursor.getInt(primaryKeyIndex) != 0 || requiresValue
                    put(cursor.getString(nameIndex), Column(cursor.getString(typeIndex), required))
                }
            }
        }

    private data class Column(
        val type: String,
        val required: Boolean,
    )
}

internal fun String.sqlIdentifier(): String {
    val quote = '"'
    return "$quote${replace(quote.toString(), "$quote$quote")}$quote"
}
