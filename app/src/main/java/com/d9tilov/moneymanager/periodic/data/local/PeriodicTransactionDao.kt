package com.d9tilov.moneymanager.periodic.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.periodic.data.local.entity.PeriodicTransactionDbModel
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
abstract class PeriodicTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(budget: PeriodicTransactionDbModel): Completable

    @Query("SELECT * FROM periodicTransaction WHERE clientId=:uid AND type=:transactionType")
    abstract fun getAll(
        uid: String,
        transactionType: TransactionType
    ): Flowable<List<PeriodicTransactionDbModel>>

    @Update
    abstract fun update(budget: PeriodicTransactionDbModel): Completable

    @Delete
    abstract fun delete(budget: PeriodicTransactionDbModel): Completable
}
