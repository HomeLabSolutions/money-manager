package com.d9tilov.moneymanager.prepopulate.currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.prepopulate.currency.data.local.entity.CurrencyDbModel
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
abstract class CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(currency: CurrencyDbModel): Completable

    @Update
    abstract fun update(currency: CurrencyDbModel): Completable

    @Query("SELECT * FROM currency")
    abstract fun getAll(): Maybe<List<CurrencyDbModel>>
}
