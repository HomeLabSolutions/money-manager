package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.d9tilov.android.database.entity.CurrencyDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyListDao {
    @Upsert
    suspend fun insert(list: List<CurrencyDbModel>)

    @Update
    suspend fun update(currency: CurrencyDbModel)

    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<CurrencyDbModel>>

    @Query("SELECT * FROM currency WHERE id=:code")
    suspend fun getByCode(code: String): CurrencyDbModel

    @Query("SELECT lastUpdateTime FROM currency WHERE id=:code")
    suspend fun getLastUpdateTime(code: String): Long
}
