package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.d9tilov.android.database.entity.InsightDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface InsightDao {
    @Query("SELECT * FROM insights WHERE clientId = :clientId ORDER BY createdAtMillis ASC")
    fun observeHistory(clientId: String): Flow<List<InsightDbModel>>

    @Query("SELECT text FROM insights WHERE clientId = :clientId ORDER BY createdAtMillis DESC, id DESC LIMIT :limit")
    suspend fun getPrevious(
        clientId: String,
        limit: Int,
    ): List<String>

    @Query(
        "SELECT EXISTS(SELECT 1 FROM insights WHERE clientId = :clientId " +
            "AND createdAtMillis >= :from AND createdAtMillis < :to)",
    )
    suspend fun hasInsightInPeriod(
        clientId: String,
        from: Long,
        to: Long,
    ): Boolean

    @Insert
    suspend fun insert(insight: InsightDbModel)

    @Query(
        "DELETE FROM insights WHERE clientId = :clientId AND id NOT IN " +
            "(SELECT id FROM insights WHERE clientId = :clientId ORDER BY createdAtMillis DESC, id DESC LIMIT :limit)",
    )
    suspend fun keepLatest(
        clientId: String,
        limit: Int,
    )

    @Transaction
    suspend fun insertAndKeepLatest(
        insight: InsightDbModel,
        limit: Int,
    ) {
        insert(insight)
        keepLatest(insight.clientId, limit)
    }
}
