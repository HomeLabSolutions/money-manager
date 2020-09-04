package com.d9tilov.moneymanager.fixed.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.fixed.data.local.entity.FixedTransactionDbModel
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
abstract class FixedTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(budget: FixedTransactionDbModel): Completable

    @Query("SELECT * FROM fixed WHERE clientId=:uid AND type=:transactionType")
    abstract fun getAll(
        uid: String,
        transactionType: TransactionType
    ): Flowable<List<FixedTransactionDbModel>>

    @Update
    abstract fun update(budget: FixedTransactionDbModel): Completable

    @Delete
    abstract fun delete(budget: FixedTransactionDbModel): Completable
}
