package com.d9tilov.moneymanager.budget.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.budget.data.local.entity.BudgetDbModel

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetDbModel)

    @Query("SELECT * FROM budget WHERE clientId=:uid")
    suspend fun get(uid: String): BudgetDbModel

    @Query("SELECT COUNT(*)  FROM budget WHERE clientId=:uid")
    suspend fun getCount(uid: String): Int

    @Update
    suspend fun update(budget: BudgetDbModel)

    @Delete
    suspend fun delete(budget: BudgetDbModel)
}
