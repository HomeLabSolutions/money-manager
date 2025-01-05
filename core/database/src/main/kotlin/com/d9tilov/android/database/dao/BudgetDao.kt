package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.d9tilov.android.database.entity.BudgetDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Upsert
    suspend fun insert(budget: BudgetDbModel)

    @Query("SELECT * FROM budget WHERE clientId=:uid")
    fun get(uid: String): Flow<BudgetDbModel?>

    @Update
    suspend fun update(budget: BudgetDbModel)

    @Delete
    suspend fun delete(budget: BudgetDbModel)
}
