package com.d9tilov.moneymanager.accumulations.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.d9tilov.moneymanager.accumulations.data.local.entitiy.AccumulationDbModel
import io.reactivex.Completable

@Dao
abstract class AccumulationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(accumulation: AccumulationDbModel): Completable

    @Update
    abstract fun update(accumulation: AccumulationDbModel): Completable
}
