package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.d9tilov.android.database.entity.MainCurrencyDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MainCurrencyDao {
    @Upsert
    suspend fun insert(mainCurrencyDbModel: MainCurrencyDbModel)

    @Query("SELECT * FROM main_currency WHERE clientId=:uid")
    fun get(uid: String): Flow<MainCurrencyDbModel?>
}
