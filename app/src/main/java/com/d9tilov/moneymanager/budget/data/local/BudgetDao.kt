package com.d9tilov.moneymanager.budget.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.entity.BudgetDbModel
import io.reactivex.Completable

@Dao
abstract class BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(budget: BudgetDbModel): Completable

    @Update
    abstract fun update(budget: BudgetDbModel): Completable

    @Delete
    abstract fun delete(budget: BudgetDbModel): Completable
}
