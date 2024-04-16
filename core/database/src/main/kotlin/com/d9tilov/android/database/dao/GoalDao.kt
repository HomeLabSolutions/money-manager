package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.android.database.entity.GoalDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Upsert
    suspend fun insert(goal: GoalDbModel)

    @Query("SELECT * FROM goal WHERE clientId=:uid")
    fun getAll(uid: String): Flow<List<GoalDbModel>>

    @Update
    suspend fun update(goal: GoalDbModel)

    @Delete
    suspend fun delete(goal: GoalDbModel)
}
