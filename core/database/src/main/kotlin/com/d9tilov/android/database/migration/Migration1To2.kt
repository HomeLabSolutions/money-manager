package com.d9tilov.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
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
