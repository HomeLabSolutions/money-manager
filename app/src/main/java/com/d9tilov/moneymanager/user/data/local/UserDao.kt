package com.d9tilov.moneymanager.user.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class UserDao {

    @Query("SELECT * FROM Users WHERE uid = :id")
    abstract fun getById(id: String): Flowable<UserDbModel>

    @Query("SELECT currencyCode FROM Users WHERE uid = :id")
    abstract fun getCurrency(id: String): Single<String>

    @Query("SELECT showPrepopulate FROM Users WHERE uid = :id")
    abstract fun showPrepopulate(id: String): Single<Boolean>

    @Query("SELECT backupData FROM Users WHERE uid = :id")
    abstract fun getBackupData(id: String): Flowable<BackupData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: UserDbModel): Completable

    @Update
    abstract fun update(user: UserDbModel): Completable

    @Query("DELETE FROM users WHERE uid=:uid")
    abstract fun delete(uid: String): Completable
}
