package com.d9tilov.android.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.database.entity.TransactionMinMaxDateDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TransactionDao {
    @Query(TRANSACTION_LIST_QUERY)
    fun getAllByType(
        clientId: String,
        from: LocalDateTime,
        to: LocalDateTime,
        type: Int,
    ): PagingSource<Int, TransactionDbModel>

    @Query(TRANSACTION_SEARCH_QUERY)
    fun searchByTypeQuery(
        clientId: String,
        from: LocalDateTime,
        to: LocalDateTime,
        type: Int,
        textPattern: String,
        amountQuery: String?,
    ): PagingSource<Int, TransactionDbModel>

    @Query(
        """
        SELECT * FROM transactions
        WHERE clientId = :clientId
            AND type = :type
            AND date >= :from
            AND date <= :to
            AND (:onlyInStatistics = 0 OR inStatistics = 1)
            AND (:withRegular = 1 OR isRegular = 0)
        """,
    )
    fun getAllByTypeInPeriod(
        clientId: String,
        from: LocalDateTime,
        to: LocalDateTime,
        type: Int,
        onlyInStatistics: Boolean,
        withRegular: Boolean,
    ): Flow<List<TransactionDbModel>>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND id = :id")
    fun getById(
        uid: String,
        id: Long,
    ): Flow<TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId")
    suspend fun getByCategoryId(
        uid: String,
        categoryId: Long,
    ): List<TransactionDbModel>

    @Query(
        """
        SELECT * FROM transactions
        WHERE clientId = :uid
            AND categoryId = :categoryId
            AND date >= :from
            AND date <= :to
            AND (:onlyInStatistics = 0 OR inStatistics = 1)
        """,
    )
    fun getByCategoryIdInPeriod(
        uid: String,
        categoryId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
        onlyInStatistics: Boolean,
    ): Flow<List<TransactionDbModel>>

    @Upsert
    suspend fun insert(transaction: TransactionDbModel)

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND date >= :from AND date <= :to")
    suspend fun getItemsCountInDay(
        uid: String,
        type: Int,
        from: LocalDateTime,
        to: LocalDateTime,
    ): Int

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND currency=:code")
    suspend fun getCountByCurrencyCode(
        uid: String,
        code: String,
    ): Int

    @Query("SELECT MIN(date) as minDate, MAX(date) as maxDate FROM transactions WHERE clientId=:uid")
    suspend fun getMinMaxDate(uid: String): TransactionMinMaxDateDbModel

    @Update
    suspend fun update(transaction: TransactionDbModel)

    @Query("DELETE FROM transactions WHERE clientId=:clientId AND id=:id")
    suspend fun delete(
        clientId: String,
        id: Long,
    )

    @Query(
        """
        DELETE FROM transactions WHERE rowId in(
            SELECT rowId FROM transactions WHERE clientId=:clientId AND categoryId=:categoryId LIMIT 1
        )
        """,
    )
    suspend fun deleteByCategoryId(
        clientId: String,
        categoryId: Long,
    )

    @Query("DELETE FROM transactions WHERE clientId=:clientId")
    suspend fun clearAll(clientId: String)
}

internal const val TRANSACTION_LIST_QUERY = """
    SELECT * FROM transactions
    WHERE clientId = :clientId AND type = :type AND date >= :from AND date <= :to
    ORDER BY date DESC, id DESC
"""

internal const val TRANSACTION_SEARCH_QUERY = """
    SELECT t.* FROM transactions AS t
    WHERE t.clientId = :clientId AND t.type = :type AND t.date >= :from AND t.date <= :to
        AND (
            t.description GLOB :textPattern
            OR t.placeName GLOB :textPattern
            OR EXISTS (
                SELECT 1 FROM categories AS c
                WHERE c.id = t.categoryId AND c.clientId = t.clientId AND c.name GLOB :textPattern
            )
            OR (:amountQuery IS NOT NULL AND instr(printf('%.2f', CAST(t.sum AS REAL) / 100.0), :amountQuery) > 0)
        )
    ORDER BY t.date DESC, t.id DESC
"""
