package com.d9tilov.android.database.dao

import androidx.sqlite.db.SupportSQLiteDatabase
import com.d9tilov.android.database.migration.MIGRATION_2_3
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.lang.reflect.Proxy
import java.sql.DriverManager

class TransactionPlaceMigrationTest {
    @Test
    fun `migration from 2 to 3 preserves expenses and adds nullable place name and address`() {
        DriverManager.getConnection("jdbc:sqlite::memory:").use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("CREATE TABLE transactions (id INTEGER PRIMARY KEY, description TEXT NOT NULL)")
                statement.execute("INSERT INTO transactions VALUES (1, 'Existing expense')")
            }
            val database =
                Proxy.newProxyInstance(
                    SupportSQLiteDatabase::class.java.classLoader,
                    arrayOf(SupportSQLiteDatabase::class.java),
                ) { _, method, arguments ->
                    check(method.name == "execSQL")
                    connection.createStatement().use { it.execute(arguments?.first() as String) }
                    null
                } as SupportSQLiteDatabase
            MIGRATION_2_3.migrate(database)
            connection.createStatement().use { statement ->
                statement
                    .executeQuery("SELECT description, placeName, placeAddress FROM transactions WHERE id = 1")
                    .use { row ->
                        check(row.next())
                        assertEquals("Existing expense", row.getString("description"))
                        assertNull(row.getString("placeName"))
                        assertNull(row.getString("placeAddress"))
                    }
                statement.execute(
                    "UPDATE transactions SET placeName = 'My cafe', placeAddress = 'High Street' WHERE id = 1",
                )
                statement.executeQuery("SELECT placeName, placeAddress FROM transactions WHERE id = 1").use { row ->
                    check(row.next())
                    assertEquals("My cafe", row.getString("placeName"))
                    assertEquals("High Street", row.getString("placeAddress"))
                }
            }
        }
    }
}
