package com.d9tilov.moneymanager.user.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE uid = :id")
    fun getById(id: String): Flow<UserDbModel>

    @Query("SELECT currentCurrencyCode FROM Users WHERE uid = :id")
    suspend fun getCurrentCurrency(id: String): String

    @Query("SELECT fiscalDay FROM Users WHERE uid = :id")
    fun getFiscalDay(id: String): Int

    @Query("SELECT showPrepopulate FROM Users WHERE uid = :id")
    suspend fun showPrepopulate(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDbModel)

    @Update
    suspend fun update(user: UserDbModel)

    @Query("DELETE FROM users WHERE uid=:uid")
    suspend fun deleteUser(uid: String)
}
