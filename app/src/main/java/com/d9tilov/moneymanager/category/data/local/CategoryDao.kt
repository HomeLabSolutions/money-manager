package com.d9tilov.moneymanager.category.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
abstract class CategoryDao {

    @Query("SELECT * FROM categories WHERE clientId=:uid AND type=:type")
    abstract fun getAllByType(uid: String, type: CategoryType): Flowable<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE clientId=:uid AND id=:id")
    abstract fun getById(uid: String, id: Long): Maybe<CategoryDbModel>

    @Query("SELECT * FROM categories WHERE clientId=:uid AND name=:name")
    abstract fun getByName(uid: String, name: String): Flowable<CategoryDbModel>

    @Query("SELECT * FROM categories WHERE clientId=:uid AND parentId=:id")
    abstract fun getByParentId(uid: String, id: Long): Maybe<List<CategoryDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun create(category: CategoryDbModel): Completable

    @Update
    abstract fun update(category: CategoryDbModel): Completable

    @Query("DELETE FROM categories WHERE id=:id")
    abstract fun delete(id: Long): Completable
}
