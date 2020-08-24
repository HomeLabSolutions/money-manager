package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Date

@Dao
abstract class TransactionDao {

    @Query("SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND date(date/1000, 'unixepoch') >= date(:from/1000, 'unixepoch', 'start of day') AND date(date/1000, 'unixepoch') <= date(:to/1000, 'unixepoch', 'start of day', '+1 day', '-1 second') ORDER BY date DESC")
    abstract fun getAllByType(
        clientId: String,
        from: Date,
        to: Date,
        type: TransactionType
    ): DataSource.Factory<Int, TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND id = :id")
    abstract fun getById(uid: String, id: Long): Flowable<TransactionDbModel>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId")
    abstract fun getByCategoryId(uid: String, categoryId: Long): Flowable<List<TransactionDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(transaction: TransactionDbModel): Completable

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND isDate=1 AND date(:date/1000, 'unixepoch') >= date(date/1000, 'unixepoch', 'start of day') AND date(:date/1000, 'unixepoch') <= date(date/1000, 'unixepoch', 'start of day', '+1 day', '-1 second')")
    abstract fun getDateItemsCountInDay(uid: String, type: TransactionType, date: Date): Single<Int>

    @Query("SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND isDate=0 AND date(:date/1000, 'unixepoch') >= date(date/1000, 'unixepoch', 'start of day') AND date(:date/1000, 'unixepoch') <= date(date/1000, 'unixepoch', 'start of day', '+1 day', '-1 second')")
    abstract fun getItemsCountInDay(uid: String, type: TransactionType, date: Date): Single<Int>

    @Update
    abstract fun update(transaction: TransactionDbModel): Completable

    @Query("DELETE FROM transactions WHERE clientId=:clientId AND id=:id")
    abstract fun delete(clientId: String, id: Long): Completable

    @Query("DELETE FROM transactions WHERE rowId in(SELECT rowId from transactions WHERE clientId=:clientId AND categoryId=:categoryId LIMIT 1)")
    abstract fun deleteByCategoryId(clientId: String, categoryId: Long): Completable

    @Query("DELETE FROM transactions WHERE clientId=:clientId AND type=:type AND isDate=1 AND date(:date/1000, 'unixepoch') >= date(date/1000, 'unixepoch', 'start of day') AND date(:date/1000, 'unixepoch') <= date(date/1000, 'unixepoch', 'start of day', '+1 day', '-1 second')")
    abstract fun deleteDate(clientId: String, type: TransactionType, date: Date): Completable

    @Query("DELETE FROM transactions WHERE clientId=:clientId")
    abstract fun clearAll(clientId: String): Completable
}
