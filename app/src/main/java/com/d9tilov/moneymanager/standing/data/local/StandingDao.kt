package com.d9tilov.moneymanager.standing.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.d9tilov.moneymanager.standing.data.local.entity.StandingDbModel
import io.reactivex.Completable

@Dao
abstract class StandingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(budget: StandingDbModel): Completable

    @Update
    abstract fun update(budget: StandingDbModel): Completable

    @Delete
    abstract fun delete(budget: StandingDbModel): Completable
}
