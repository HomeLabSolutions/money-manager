package com.d9tilov.android.database

import android.database.Cursor
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.lang.reflect.Proxy
import java.sql.Connection
import java.sql.DriverManager

class DatabaseBackupSchemaValidatorTest {
    private val source = DriverManager.getConnection("jdbc:sqlite::memory:")
    private val destination = DriverManager.getConnection("jdbc:sqlite::memory:")

    @After
    fun tearDown() {
        source.close()
        destination.close()
    }

    @Test
    fun `missing nullable and defaulted columns are compatible without version exceptions`() {
        execute(source, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT NOT NULL)")
        execute(source, "INSERT INTO entries VALUES (1, 'Existing entry')")
        execute(
            destination,
            """
            CREATE TABLE entries (
                id INTEGER PRIMARY KEY, name TEXT NOT NULL, note TEXT, active INTEGER NOT NULL DEFAULT 1
            )
            """.trimIndent(),
        )

        validate()

        execute(destination, "INSERT INTO entries (id, name) VALUES (1, 'Existing entry')")
        destination.createStatement().use { statement ->
            statement.executeQuery("SELECT note, active FROM entries").use { row ->
                check(row.next())
                assertEquals(null, row.getString("note"))
                assertEquals(1, row.getInt("active"))
            }
        }
    }

    @Test
    fun `different indices and foreign key definitions do not prevent restoring compatible columns`() {
        execute(source, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT NOT NULL)")
        execute(source, "CREATE INDEX backup_name_index ON entries(name)")
        execute(destination, "CREATE TABLE names (name TEXT PRIMARY KEY)")
        execute(destination, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT NOT NULL REFERENCES names(name))")

        validate()
    }

    @Test
    fun `missing table is rejected even when it has no rows to copy`() {
        execute(destination, "CREATE TABLE entries (id INTEGER PRIMARY KEY)")

        assertThrows(IllegalStateException::class.java) { validate() }
    }

    @Test
    fun `missing required column is rejected even when the source has no rows`() {
        execute(source, "CREATE TABLE entries (id INTEGER PRIMARY KEY)")
        execute(destination, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT NOT NULL)")

        assertThrows(IllegalStateException::class.java) { validate() }
    }

    @Test
    fun `missing primary key is rejected rather than generating new ids`() {
        execute(source, "CREATE TABLE entries (name TEXT)")
        execute(destination, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT)")

        assertThrows(IllegalStateException::class.java) { validate() }
    }

    @Test
    fun `incompatible types and unknown columns are rejected before copying`() {
        execute(source, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name INTEGER)")
        execute(destination, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT)")

        assertThrows(IllegalStateException::class.java) { validate() }

        execute(source, "DROP TABLE entries")
        execute(source, "CREATE TABLE entries (id INTEGER PRIMARY KEY, name TEXT, extra TEXT)")

        assertThrows(IllegalStateException::class.java) { validate() }
    }

    private fun validate() {
        DatabaseBackupSchemaValidator.validate(
            sourceQuery = { source.queryCursor(it) },
            destinationQuery = { destination.queryCursor(it) },
            tables = listOf("entries"),
        )
    }

    private fun execute(
        connection: Connection,
        sql: String,
    ) {
        connection.createStatement().use { it.execute(sql) }
    }

    private fun Connection.queryCursor(sql: String): Cursor {
        val rows = mutableListOf<List<Any?>>()
        val columnNames =
            createStatement().use { statement ->
                statement.executeQuery(sql).use { result ->
                    val names = (1..result.metaData.columnCount).map(result.metaData::getColumnLabel)
                    while (result.next()) rows.add(names.indices.map { result.getObject(it + 1) })
                    names
                }
            }
        var position = -1
        return Proxy.newProxyInstance(Cursor::class.java.classLoader, arrayOf(Cursor::class.java)) { _, method, args ->
            when (method.name) {
                "moveToNext" -> ++position < rows.size
                "getColumnIndexOrThrow" -> columnNames.indexOf(args?.first() as String).also { check(it >= 0) }
                "getString" -> rows[position][args?.first() as Int]?.toString()
                "getInt" -> (rows[position][args?.first() as Int] as Number).toInt()
                "isNull" -> rows[position][args?.first() as Int] == null
                "close" -> null
                else -> error("Unexpected cursor method: ${method.name}")
            }
        } as Cursor
    }
}
