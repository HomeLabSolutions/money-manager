package com.d9tilov.android.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "insights", indices = [Index(value = ["clientId", "createdAtMillis"])])
data class InsightDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: String,
    val createdAtMillis: Long,
    val text: String,
)
