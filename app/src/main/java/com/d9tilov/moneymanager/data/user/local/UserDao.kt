package com.d9tilov.moneymanager.data.user.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.data.user.local.entities.UserDbModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE uid = :id")
    fun getById(id: String): Flowable<UserDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserDbModel): Completable

    @Update
    fun update(user: UserDbModel): Completable

    @Query("DELETE FROM users WHERE uid=:uid")
    fun delete(uid: String): Completable
}
