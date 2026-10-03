package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.d9tilov.android.database.entity.InsightDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface InsightDao {
    @Query(
        "SELECT * FROM insights WHERE clientId = :clientId " +
            "AND createdDate >= :from AND createdDate < :to ORDER BY createdDate ASC",
    )
    fun get(
        clientId: String,
        from: Long,
        to: Long,
    ): Flow<List<InsightDbModel>>

    @Upsert
    suspend fun upsert(insight: InsightDbModel)

    @Query(
        "DELETE FROM insights WHERE clientId = :clientId AND id NOT IN " +
            "(SELECT id FROM insights WHERE clientId = :clientId ORDER BY createdDate DESC, id DESC LIMIT :limit)",
    )
    suspend fun keepLatest(
        clientId: String,
        limit: Int,
    )

    @Transaction
    suspend fun upsertAndKeepLatest(
        insight: InsightDbModel,
        limit: Int,
    ) {
        upsert(insight)
        keepLatest(insight.clientId, limit)
    }
}
