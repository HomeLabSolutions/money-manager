package com.d9tilov.moneymanager.goal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.goal.data.local.entity.GoalDbModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
abstract class GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(goal: GoalDbModel): Completable

    @Query("SELECT * FROM goal WHERE clientId=:uid")
    abstract fun getAll(
        uid: String
    ): Flowable<List<GoalDbModel>>

    @Update
    abstract fun update(goal: GoalDbModel): Completable

    @Delete
    abstract fun delete(goal: GoalDbModel): Completable
}
