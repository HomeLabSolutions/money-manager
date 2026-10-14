package com.d9tilov.android.database.dao

import com.d9tilov.android.database.converters.CurrencyConverter
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.sqlite.SQLiteDataSource
import java.math.BigDecimal

class TransactionSearchQueryTest {
    private val database = SQLiteDataSource().apply { url = "jdbc:sqlite::memory:" }.connection

    @Before
    fun setUp() {
        database.createStatement().use { statement ->
            statement.execute("CREATE TABLE categories (id INTEGER, clientId TEXT, name TEXT)")
            statement.execute(
                """
                CREATE TABLE transactions (
                    id INTEGER, clientId TEXT, type INTEGER, categoryId INTEGER,
                    date INTEGER, description TEXT, sum TEXT, currency TEXT
                )
                """.trimIndent(),
            )
            statement.execute("INSERT INTO categories VALUES (1, 'user', 'Продукты')")
        }
        val insert = "INSERT INTO transactions VALUES (?, 'user', 0, 1, ?, ?, ?, 'USD')"
        database.prepareStatement(insert).use { statement ->
            for (id in 1..50) {
                statement.setInt(1, id)
                statement.setInt(2, 100 - id)
                statement.setString(3, "Запись $id")
                statement.setString(4, CurrencyConverter.fromBigDecimalToString(BigDecimal("10.00")))
                statement.addBatch()
            }
            statement.executeBatch()
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `search finds an unloaded transaction in the first filtered page`() {
        assertEquals((1..10).toList(), search(""))
        assertEquals(listOf(50), search("зАПИСЬ 50"))
    }

    @Test
    fun `filtered pages contain only matches in date order`() {
        database.createStatement().use { it.execute("UPDATE transactions SET description = 'Match' WHERE id >= 25") }

        assertEquals((25..34).toList(), search("match"))
        assertEquals((35..44).toList(), search("match", offset = 10))
        assertEquals((45..50).toList(), search("match", offset = 20))
    }

    @Test
    fun `category and description match Cyrillic and Latin text ignoring case`() {
        assertEquals((1..10).toList(), search(" ПРОДУ "))
        updateDescription("Покупка в МаГаЗиНе у дома Café")
        assertEquals(listOf(50), search("МАГАЗИНЕ У ДОМА"))
        assertEquals(listOf(50), search("CAFÉ"))
    }

    @Test
    fun `amount search uses the unscaled sum with decimal separators and grouping`() {
        updateAmount("1234.50")
        listOf("234", "1234.5", "1234,50", "1 234,50", "1\u00a0234,50").forEach { query ->
            assertEquals(query, listOf(50), search(query))
        }
        assertTrue(search("1234,51").isEmpty())
        assertTrue(search("123450").isEmpty())
        updateAmount("0.05")
        assertEquals(listOf(50), search("0,05"))
        updateAmount("99999999.99")
        assertEquals(listOf(50), search("99999999,99"))
    }

    @Test
    fun `numeric queries match the amount category and description`() {
        updateAmount("1234.50")
        database.createStatement().use { statement ->
            statement.execute("UPDATE categories SET name = 'Category 1234'")
            statement.execute("UPDATE transactions SET description = '1234' WHERE id = 49")
        }

        assertEquals((1..10).toList(), search("1234"))
        database.createStatement().use { it.execute("UPDATE categories SET name = 'Other'") }
        assertEquals(listOf(49, 50), search("1234"))
        assertTrue(search("001234.500").isEmpty())
        assertEquals((1..10).toList(), search("10"))
    }

    @Test
    fun `mixed text and digits search description and category`() {
        updateAmount("1234.50")
        updateDescription("Order 1234")

        assertEquals(listOf(50), search("Order 1234"))
        assertTrue(search("1234 groceries").isEmpty())
        database.createStatement().use { it.execute("UPDATE categories SET name = '1234 groceries'") }
        assertEquals((1..10).toList(), search("1234 groceries"))
    }

    @Test
    fun `wildcards and punctuation are searched literally`() {
        updateDescription("Discount 10%_*?[x] \\ quote'🙂")

        listOf("%", "_", "*", "?", "[x]", "]", "\\", "'", "🙂").forEach { query ->
            assertEquals(query, listOf(50), search(query))
        }
    }

    @Test
    fun `search respects current user type and date bounds`() {
        database.createStatement().use { statement ->
            statement.execute("UPDATE transactions SET description = 'Match', clientId = 'other' WHERE id = 47")
            statement.execute("UPDATE transactions SET description = 'Match', type = 1 WHERE id = 48")
            statement.execute("UPDATE transactions SET description = 'Match', date = 1000 WHERE id = 49")
            statement.execute("UPDATE transactions SET description = 'Match' WHERE id = 50")
        }
        assertEquals(listOf(50), search("match"))
    }

    @Test
    fun `clearing search restores the list and unrelated fields do not match`() {
        assertEquals(listOf(50), search("Запись 50"))
        assertEquals((1..10).toList(), search("   "))
        assertEquals((11..20).toList(), search("", offset = 10))
        assertEquals((41..50).toList(), search("   ", offset = 40))
        assertTrue(search("USD").isEmpty())
        assertTrue(search("Not found").isEmpty())
    }

    private fun search(
        query: String,
        offset: Int = 0,
    ): List<Int> {
        val searchQuery = query.takeIf { it.isNotBlank() }?.let(::toSearchQuery)
        val sql = if (searchQuery == null) TRANSACTION_LIST_QUERY else TRANSACTION_SEARCH_QUERY
        return database.prepareStatement("$sql LIMIT ? OFFSET ?").use { statement ->
            statement.setString(1, "user")
            statement.setInt(2, 0)
            statement.setLong(3, 0)
            statement.setLong(4, 100)
            val pageParameterIndex =
                if (searchQuery == null) {
                    5
                } else {
                    statement.setString(5, searchQuery.textPattern)
                    statement.setString(6, searchQuery.amountQuery?.toPlainString())
                    7
                }
            statement.setInt(pageParameterIndex, 10)
            statement.setInt(pageParameterIndex + 1, offset)
            statement.executeQuery().use { result ->
                buildList {
                    while (result.next()) add(result.getInt("id"))
                }
            }
        }
    }

    private fun toSearchQuery(query: String): TransactionSearchQuery {
        val amountQuery =
            query
                .replace(',', '.')
                .filterNot { it.isWhitespace() }
                .toBigDecimalOrNull()
        return TransactionSearchQuery(
            amountQuery = amountQuery,
            textPattern = query.trim().map(::globCharacter).joinToString(separator = "", prefix = "*", postfix = "*"),
        )
    }

    private fun globCharacter(character: Char): String =
        when (character) {
            '*' -> {
                "[*]"
            }

            '?' -> {
                "[?]"
            }

            '[' -> {
                "[[]"
            }

            else -> {
                if (character.lowercaseChar() != character.uppercaseChar()) {
                    "[$character${character.uppercaseChar()}${character.lowercaseChar()}]"
                } else {
                    character.toString()
                }
            }
        }

    private fun updateDescription(description: String) {
        database.prepareStatement("UPDATE transactions SET description = ? WHERE id = 50").use { statement ->
            statement.setString(1, description)
            statement.executeUpdate()
        }
    }

    private fun updateAmount(amount: String) {
        database.prepareStatement("UPDATE transactions SET sum = ? WHERE id = 50").use { statement ->
            statement.setString(1, CurrencyConverter.fromBigDecimalToString(BigDecimal(amount)))
            statement.executeUpdate()
        }
    }
}
