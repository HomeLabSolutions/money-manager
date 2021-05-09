package com.d9tilov.moneymanager.user.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE uid = :id")
    fun getById(id: String): Flow<UserDbModel>

    @Query("SELECT currencyCode FROM Users WHERE uid = :id")
    suspend fun getCurrency(id: String): String

    @Query("SELECT showPrepopulate FROM Users WHERE uid = :id")
    suspend fun showPrepopulate(id: String): Boolean

    @Query("SELECT backupData FROM Users WHERE uid = :id")
    fun getBackupData(id: String): Flow<BackupData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDbModel)

    @Update
    suspend fun update(user: UserDbModel)

    @Query("DELETE FROM users WHERE uid=:uid")
    suspend fun delete(uid: String)
}
