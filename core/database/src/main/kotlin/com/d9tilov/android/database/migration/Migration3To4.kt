package com.d9tilov.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.database.converters.CurrencyConverter

private const val FROM_VERSION = 3
private const val TO_VERSION = 4

val MIGRATION_3_4 =
    object : Migration(FROM_VERSION, TO_VERSION) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE transactions RENAME TO transactions_v3")
            db.execSQL(CREATE_TRANSACTIONS)
            val usdPrecision = Currency.USD.precision
            db.query("SELECT id, currency, sum, usd_sum FROM transactions_v3").use { rows ->
                val usdSumIndex = rows.getColumnIndexOrThrow("usd_sum")
                while (rows.moveToNext()) {
                    val id = rows.getLong(0)
                    val currency = Currency.fromCode(rows.getString(1))
                    val money = currency?.let { decodeLegacyAmount(rows.getString(2))?.toMoney(it) } ?: continue
                    val usdCents =
                        decodeLegacyAmount(rows.getString(usdSumIndex))
                            ?.roundTo(usdPrecision)
                            ?.toLongExact(usdPrecision)
                    if (usdCents != null) {
                        db.execSQL(COPY_TRANSACTION, arrayOf(money.value, usdCents, id))
                    }
                }
            }
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'transactions'")
            db.execSQL("UPDATE sqlite_sequence SET name = 'transactions' WHERE name = 'transactions_v3'")
            db.execSQL("DROP TABLE transactions_v3")
            db.execSQL("CREATE INDEX index_transactions_clientId ON transactions (clientId)")
            removeUnsupportedCurrencies(db)
        }

        private fun removeUnsupportedCurrencies(db: SupportSQLiteDatabase) {
            val codes = Currency.entries.map(Currency::name)
            val parameters = codes.joinToString { "?" }
            db.execSQL("DELETE FROM currency WHERE id NOT IN ($parameters)", codes.toTypedArray())
            db.execSQL(
                "UPDATE main_currency SET code = ? WHERE code NOT IN ($parameters)",
                (listOf(DEFAULT_CURRENCY_CODE) + codes).toTypedArray(),
            )
        }

        private fun decodeLegacyAmount(stored: String): Decimal? =
            try {
                CurrencyConverter.fromStringToDecimal(stored)
            } catch (_: IllegalArgumentException) {
                null
            }
    }

private const val CREATE_TRANSACTIONS = """
    CREATE TABLE transactions (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        clientId TEXT NOT NULL,
        type INTEGER NOT NULL,
        categoryId INTEGER NOT NULL,
        currency TEXT NOT NULL,
        sum INTEGER NOT NULL,
        usd_sum INTEGER NOT NULL,
        date INTEGER NOT NULL,
        description TEXT NOT NULL,
        qrCode TEXT,
        isRegular INTEGER NOT NULL,
        inStatistics INTEGER NOT NULL,
        latitude REAL NOT NULL,
        longitude REAL NOT NULL,
        photo TEXT,
        placeName TEXT,
        placeAddress TEXT,
        FOREIGN KEY(clientId) REFERENCES users(uid) ON UPDATE NO ACTION ON DELETE CASCADE
    )
"""

private const val COPY_TRANSACTION = """
    INSERT INTO transactions (
        id, clientId, type, categoryId, currency, sum, usd_sum,
        date, description, qrCode, isRegular, inStatistics, latitude, longitude, photo, placeName, placeAddress
    )
    SELECT id, clientId, type, categoryId, currency, ?, ?,
        date, description, qrCode, isRegular, inStatistics, latitude, longitude, photo, placeName, placeAddress
    FROM transactions_v3
    WHERE id = ?
"""
