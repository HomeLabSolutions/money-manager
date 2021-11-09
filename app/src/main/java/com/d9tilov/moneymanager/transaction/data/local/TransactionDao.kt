package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND date >= :from AND date <= :to ORDER BY date DESC")
    fun getAllByType(
        clientId: String,
        from: Date,
        to: Date,
        type: TransactionType
    ): PagingSource<Int, TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND isDate=0 AND isRegular=0 AND date >= :from AND date <= :to")
    fun getAllByTypeInPeriod(
        clientId: String,
        from: Date,
        to: Date,
        type: TransactionType
    ): Flow<List<TransactionDbModel>>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND id = :id")
    fun getById(uid: String, id: Long): Flow<TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId")
    fun getByCategoryId(uid: String, categoryId: Long): Flow<List<TransactionDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionDbModel)

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND isDate=1 AND date(:date/1000, 'unixepoch') >= date(date/1000, 'unixepoch', 'start of day') AND date(:date/1000, 'unixepoch') <= date(date/1000, 'unixepoch', 'start of day', '+1 day', '-1 second')")
    suspend fun getDateItemsCountInDay(
        uid: String,
        type: TransactionType,
        date: Date
    ): Int

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND isDate=0 AND date(:date/1000, 'unixepoch') >= date(date/1000, 'unixepoch', 'start of day') AND date(:date/1000, 'unixepoch') <= date(date/1000, 'unixepoch', 'start of day', '+1 day', '-1 second')")
    suspend fun getItemsCountInDay(uid: String, type: TransactionType, date: Date): Int

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND currency=:code")
    suspend fun getCountByCurrencyCode(uid: String, code: String): Int

    @Update
    suspend fun update(transaction: TransactionDbModel)

    @Query("DELETE FROM transactions WHERE clientId=:clientId AND id=:id")
    suspend fun delete(clientId: String, id: Long)

    @Query("DELETE FROM transactions WHERE rowId in(SELECT rowId from transactions WHERE clientId=:clientId AND categoryId=:categoryId LIMIT 1)")
    suspend fun deleteByCategoryId(clientId: String, categoryId: Long)

    @Query("DELETE FROM transactions WHERE clientId=:clientId AND type=:type AND isDate=1 AND date >= :from AND date <= :to")
    suspend fun deleteDate(clientId: String, type: TransactionType, from: Date, to: Date)

    @Query("DELETE FROM transactions WHERE clientId=:clientId")
    suspend fun clearAll(clientId: String)
}
