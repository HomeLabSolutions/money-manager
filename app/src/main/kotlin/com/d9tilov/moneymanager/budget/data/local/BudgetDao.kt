package com.d9tilov.moneymanager.budget.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.budget.data.entity.BudgetDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetDbModel)

    @Query("SELECT * FROM budget WHERE clientId=:uid")
    fun get(uid: String): Flow<BudgetDbModel?>

    @Update
    suspend fun update(budget: BudgetDbModel)

    @Delete
    suspend fun delete(budget: BudgetDbModel)
}
