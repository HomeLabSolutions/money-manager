package com.d9tilov.android.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.d9tilov.android.database.di.buildAppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.File

class MoneyMigrationTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @Test
    fun onDeviceUpgradeMigratesOnceAndIgnoresUnrepresentableRows() =
        runTest {
            withTemporaryDatabase { path ->
                createVersion3(path)
                withRoom(path) { database ->
                    assertVersion4(database)
                    assertSequencePreserved(database)
                    assertInvalidRowsHidden(database)
                }
                withRoom(path, ::assertVersion4)
            }
        }

    private fun assertSequencePreserved(database: AppDatabase) {
        val sql = "SELECT seq FROM sqlite_sequence WHERE name = 'transactions'"
        database.openHelper.readableDatabase.query(sql).use { cursor ->
            cursor.moveToFirst()
            assertEquals(20L, cursor.getLong(0))
        }
    }

    private suspend fun assertInvalidRowsHidden(database: AppDatabase) {
        val dao = database.transactionDao()
        val rows =
            dao
                .getAllByTypeInPeriod(
                    UID,
                    LocalDateTime(1900, 1, 1, 0, 0),
                    LocalDateTime(2100, 1, 1, 0, 0),
                    0,
                    false,
                    true,
                ).first()
        assertEquals(setOf(1L, 2L, 3L, 4L, 5L, 6L), rows.map { it.id }.toSet())
        assertNull(dao.getById(UID, 7).first())
    }

    @Test
    fun restoresV3AndV4BackupsAndLeavesSourceV3Unmodified() =
        withTemporaryDatabase { source ->
            withTemporaryDatabase { destination ->
                withTemporaryDatabase { snapshot ->
                    createVersion3(source)
                    withRoom(destination) { database ->
                        val store = DatabaseBackupStore(database, context)
                        store.restore(source, UID)
                        assertVersion4(database)
                        store.snapshot(snapshot)
                        store.restore(snapshot, UID)
                        assertVersion4(database)
                    }
                    SQLiteDatabase.openDatabase(source.path, null, SQLiteDatabase.OPEN_READONLY).use { raw ->
                        assertEquals(3, raw.version)
                        raw.rawQuery("SELECT sum FROM transactions WHERE id = 1", null).use {
                            it.moveToFirst()
                            assertEquals("12300", it.getString(0))
                        }
                    }
                }
            }
        }

    private fun assertVersion4(database: AppDatabase) {
        val sqlite = database.openHelper.readableDatabase
        assertEquals(4, sqlite.version)
        val select = "SELECT sum, typeof(sum), sum_precision, legacy_sum, usd_sum FROM transactions ORDER BY id"
        val rows =
            sqlite.query(select).use { cursor ->
                buildList {
                    while (cursor.moveToNext()) add(readStoredAmount(cursor))
                }
            }
        assertEquals(
            listOf(
                StoredAmount(123, 0),
                StoredAmount(801, 0),
                StoredAmount(1234, 2),
                StoredAmount(-1234, 3),
                StoredAmount(1234, 4),
                StoredAmount(Long.MIN_VALUE, 0),
                StoredAmount(null, null, "10050.00"),
                StoredAmount(null, null, "500"),
                StoredAmount(null, null, "922337203685477580800"),
            ),
            rows,
        )
    }

    private fun readStoredAmount(cursor: Cursor): StoredAmount {
        val amount = if (cursor.isNull(0)) null else cursor.getLong(0)
        val scale = if (cursor.isNull(2)) null else cursor.getInt(2)
        assertEquals(if (amount == null) "null" else "integer", cursor.getString(1))
        assertEquals(USD_RAW, cursor.getString(4))
        return StoredAmount(amount, scale, cursor.getString(3))
    }

    private fun createVersion3(path: File) {
        val schema =
            instrumentation.context.assets
                .open("${AppDatabase::class.java.name}/3.json")
                .bufferedReader()
                .use { JSONObject(it.readText()).getJSONObject("database") }
        SQLiteDatabase.openOrCreateDatabase(path, null).use { database ->
            val entities = schema.getJSONArray("entities")
            repeat(entities.length()) { index ->
                val entity = entities.getJSONObject(index)

                fun String.withTableName() = replace("\${TABLE_NAME}", entity.getString("tableName"))
                database.execSQL(entity.getString("createSql").withTableName())
                val indices = entity.optJSONArray("indices")
                repeat(indices?.length() ?: 0) { indexOfIndex ->
                    val indexSql = checkNotNull(indices).getJSONObject(indexOfIndex).getString("createSql")
                    database.execSQL(indexSql.withTableName())
                }
            }
            val setup = schema.getJSONArray("setupQueries")
            repeat(setup.length()) { index ->
                database.execSQL(setup.getString(index).replace("\${ROOM_MASTER_TABLE_NAME}", "room_master_table"))
            }
            database.version = schema.getInt("version")
            seedVersion3(database)
            database.execSQL("UPDATE sqlite_sequence SET seq = 20 WHERE name = 'transactions'")
        }
    }

    private fun seedVersion3(database: SQLiteDatabase) {
        database.execSQL(
            "INSERT INTO users (uid, showPrepopulate, fiscalDay, insightLanguage) VALUES (?, 0, 1, 'en')",
            arrayOf(UID),
        )
        val oldAmounts =
            listOf(
                "JPY" to "12300",
                "KRW" to "80100",
                "USD" to "1234.00",
                "KWD" to "-123.400",
                "CLF" to "12.3400",
                "JPY" to "-922337203685477580800",
                "KRW" to "10050.00",
                "XDR" to "500",
                "JPY" to "922337203685477580800",
            )
        oldAmounts.forEach { (currency, amount) ->
            database.execSQL(
                """
                INSERT INTO transactions (
                    clientId, type, categoryId, currency, sum, usd_sum, date,
                    description, isRegular, inStatistics, latitude, longitude
                )
                VALUES (?, 0, 1, ?, ?, ?, 0, 'test', 0, 1, 0, 0)
                """.trimIndent(),
                arrayOf(UID, currency, amount, USD_RAW),
            )
        }
    }

    private inline fun withRoom(
        file: File,
        test: (AppDatabase) -> Unit,
    ) {
        val database = buildAppDatabase(context, file.path)
        try {
            test(database)
        } finally {
            database.close()
        }
    }

    private inline fun withTemporaryDatabase(test: (File) -> Unit) {
        val file = File.createTempFile("money-migration-", ".db", context.cacheDir)
        try {
            test(file)
        } finally {
            SQLiteDatabase.deleteDatabase(file)
        }
    }

    private data class StoredAmount(
        val value: Long?,
        val scale: Int?,
        val legacy: String? = null,
    )

    private companion object {
        const val UID = "money-test-user"
        const val USD_RAW = "77.57438153351307885200"
    }
}
