package com.d9tilov.moneymanager.user.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
abstract class UserDao {

    @Query("SELECT * FROM Users WHERE uid = :id")
    abstract fun getById(id: String): Flowable<UserDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: UserDbModel): Completable

    @Update
    abstract fun update(user: UserDbModel): Completable

    @Query("DELETE FROM users WHERE uid=:uid")
    abstract fun delete(uid: String): Completable
}
