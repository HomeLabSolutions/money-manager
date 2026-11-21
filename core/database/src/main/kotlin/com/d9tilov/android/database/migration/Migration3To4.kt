package com.d9tilov.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Money
import com.d9tilov.android.core.utils.toMoneyOrNull
import com.d9tilov.android.database.converters.CurrencyConverter

private const val FROM_VERSION = 3
private const val TO_VERSION = 4

val MIGRATION_3_4 =
    object : Migration(FROM_VERSION, TO_VERSION) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE transactions RENAME TO transactions_v3")
            db.execSQL(CREATE_TRANSACTIONS)
            db.query("SELECT id, currency, sum FROM transactions_v3").use { rows ->
                while (rows.moveToNext()) {
                    val id = rows.getLong(0)
                    val stored = rows.getString(2)
                    val money = decodeLegacyAmount(rows.getString(1), stored)
                    db.execSQL(
                        COPY_TRANSACTION,
                        arrayOf<Any?>(money?.value, money?.currency?.precision, stored.takeIf { money == null }, id),
                    )
                }
            }
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'transactions'")
            db.execSQL("UPDATE sqlite_sequence SET name = 'transactions' WHERE name = 'transactions_v3'")
            db.execSQL("DROP TABLE transactions_v3")
            db.execSQL("CREATE INDEX index_transactions_clientId ON transactions (clientId)")
        }

        private fun decodeLegacyAmount(
            code: String,
            stored: String,
        ): Money? {
            val currency = Currency.fromCode(code) ?: return null
            val oldAmount = stored.toBigDecimalOrNull() ?: return null
            return try {
                oldAmount.movePointLeft(CurrencyConverter.STORAGE_SCALE).toMoneyOrNull(currency)
            } catch (_: ArithmeticException) {
                null
            }
        }
    }

private const val CREATE_TRANSACTIONS = """
    CREATE TABLE transactions (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        clientId TEXT NOT NULL,
        type INTEGER NOT NULL,
        categoryId INTEGER NOT NULL,
        currency TEXT NOT NULL,
        sum INTEGER,
        sum_precision INTEGER,
        legacy_sum TEXT,
        usd_sum TEXT NOT NULL,
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
        id, clientId, type, categoryId, currency, sum, sum_precision, legacy_sum, usd_sum,
        date, description, qrCode, isRegular, inStatistics, latitude, longitude, photo, placeName, placeAddress
    )
    SELECT id, clientId, type, categoryId, currency, ?, ?, ?, usd_sum,
        date, description, qrCode, isRegular, inStatistics, latitude, longitude, photo, placeName, placeAddress
    FROM transactions_v3
    WHERE id = ?
"""
