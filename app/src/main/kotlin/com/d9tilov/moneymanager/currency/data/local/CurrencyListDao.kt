package com.d9tilov.moneymanager.currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CurrencyDbModel>)

    @Update
    suspend fun update(currency: CurrencyDbModel)

    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<CurrencyDbModel>>

    @Query("SELECT * FROM currency WHERE id=:code")
    fun getByCode(code: String): CurrencyDbModel

    @Query("SELECT lastUpdateTime FROM currency WHERE id=:code")
    fun getLastUpdateTime(code: String): Long
}
