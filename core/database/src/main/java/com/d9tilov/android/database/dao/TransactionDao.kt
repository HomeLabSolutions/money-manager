package com.d9tilov.android.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.android.database.model.TransactionType
import com.d9tilov.android.database.entity.TransactionDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND date >= :from AND date <= :to ORDER BY date DESC")
    fun getAllByType(
        clientId: String,
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType
    ): PagingSource<Int, TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND date >= :from AND date <= :to")
    fun getAllByTypeInPeriod(
        clientId: String,
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType
    ): Flow<List<TransactionDbModel>>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND id = :id")
    fun getById(uid: String, id: Long): Flow<TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId")
    suspend fun getByCategoryId(uid: String, categoryId: Long): List<TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId AND date >= :from AND date <= :to")
    fun getByCategoryIdInPeriod(
        uid: String,
        categoryId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): Flow<List<TransactionDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionDbModel)

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND date >= :from AND date <= :to")
    suspend fun getItemsCountInDay(
        uid: String,
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime
    ): Int

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND currency=:code")
    suspend fun getCountByCurrencyCode(uid: String, code: String): Int

    @Update
    suspend fun update(transaction: TransactionDbModel)

    @Query("DELETE FROM transactions WHERE clientId=:clientId AND id=:id")
    suspend fun delete(clientId: String, id: Long)

    @Query("DELETE FROM transactions WHERE rowId in(SELECT rowId from transactions WHERE clientId=:clientId AND categoryId=:categoryId LIMIT 1)")
    suspend fun deleteByCategoryId(clientId: String, categoryId: Long)

    @Query("DELETE FROM transactions WHERE clientId=:clientId")
    suspend fun clearAll(clientId: String)
}
