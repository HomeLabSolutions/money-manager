package com.d9tilov.moneymanager.regular.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.regular.data.local.entity.RegularTransactionDbModel
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface RegularTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: RegularTransactionDbModel)

    @Query("SELECT * FROM regularTransaction WHERE clientId=:uid AND type=:transactionType")
    fun getAll(
        uid: String,
        transactionType: TransactionType
    ): Flow<List<RegularTransactionDbModel>>

    @Update
    suspend fun update(budget: RegularTransactionDbModel)

    @Delete
    suspend fun delete(budget: RegularTransactionDbModel)
}
