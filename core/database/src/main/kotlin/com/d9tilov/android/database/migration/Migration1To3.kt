package com.d9tilov.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val FROM_VERSION = 1
private const val TO_VERSION = 3

val MIGRATION_1_3 =
    object : Migration(FROM_VERSION, TO_VERSION) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE users ADD COLUMN insightLanguage TEXT NOT NULL DEFAULT ''")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `insights` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `clientId` TEXT NOT NULL,
                    `createdAtMillis` INTEGER NOT NULL,
                    `text` TEXT NOT NULL
                )
                """.trimIndent(),
            )
            db.execSQL(
                """
                CREATE INDEX IF NOT EXISTS `index_insights_clientId_createdAtMillis`
                ON `insights` (`clientId`, `createdAtMillis`)
                """.trimIndent(),
            )
        }
    }
