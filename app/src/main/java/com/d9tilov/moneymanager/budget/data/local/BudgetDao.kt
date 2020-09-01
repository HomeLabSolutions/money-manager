package com.d9tilov.moneymanager.budget.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.budget.data.local.entity.BudgetDbModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(budget: BudgetDbModel): Completable

    @Query("SELECT * FROM budget WHERE clientId=:uid")
    abstract fun get(uid: String): Single<BudgetDbModel>

    @Query("SELECT COUNT(*)  FROM budget WHERE clientId=:uid")
    abstract fun getCount(uid: String): Single<Int>

    @Update
    abstract fun update(budget: BudgetDbModel): Completable

    @Delete
    abstract fun delete(budget: BudgetDbModel): Completable
}
