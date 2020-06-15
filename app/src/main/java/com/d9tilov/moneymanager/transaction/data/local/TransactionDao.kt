package com.d9tilov.moneymanager.transaction.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
abstract class TransactionDao {

    @Query("SELECT * FROM transactions WHERE clientId=:uid AND type=:type ORDER BY date DESC")
    abstract fun getAllByType(
        uid: String,
        type: TransactionType
    ): Flowable<List<TransactionDbModel>>

    @Query("SELECT * FROM transactions WHERE clientId =:uid AND id = :id")
    abstract fun getById(uid: String, id: Long): Flowable<TransactionDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(transaction: TransactionDbModel): Completable

    @Update
    abstract fun update(transaction: TransactionDbModel): Completable

    @Query("DELETE FROM transactions WHERE id=:id")
    abstract fun delete(id: Long): Completable
}
