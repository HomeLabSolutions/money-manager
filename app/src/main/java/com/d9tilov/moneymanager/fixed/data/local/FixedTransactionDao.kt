package com.d9tilov.moneymanager.fixed.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.d9tilov.moneymanager.fixed.data.local.entity.FixedTransactionDbModel
import io.reactivex.Completable

@Dao
abstract class FixedTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(budget: FixedTransactionDbModel): Completable

    @Update
    abstract fun update(budget: FixedTransactionDbModel): Completable

    @Delete
    abstract fun delete(budget: FixedTransactionDbModel): Completable
}
