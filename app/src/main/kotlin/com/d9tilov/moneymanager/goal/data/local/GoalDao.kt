package com.d9tilov.moneymanager.goal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.goal.data.entity.GoalDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalDbModel)

    @Query("SELECT * FROM goal WHERE clientId=:uid")
    fun getAll(
        uid: String
    ): Flow<List<GoalDbModel>>

    @Update
    suspend fun update(goal: GoalDbModel)

    @Delete
    suspend fun delete(goal: GoalDbModel)
}
