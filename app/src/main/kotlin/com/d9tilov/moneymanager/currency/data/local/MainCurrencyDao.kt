package com.d9tilov.moneymanager.currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d9tilov.moneymanager.currency.data.local.entity.MainCurrencyDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MainCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mainCurrencyDbModel: MainCurrencyDbModel)

    @Query("SELECT * FROM main_currency WHERE clientId=:uid")
    fun get(uid: String): Flow<MainCurrencyDbModel?>
}
