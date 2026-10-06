package com.d9tilov.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "insights", indices = [Index(value = ["clientId", "createdDate"])])
data class InsightDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "clientId") val clientId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "createdDate") val createdDate: LocalDateTime,
)
