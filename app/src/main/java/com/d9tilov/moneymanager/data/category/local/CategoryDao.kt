package com.d9tilov.moneymanager.data.category.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.data.category.local.entities.CategoryDbModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
abstract class CategoryDao {

    @Query("SELECT * FROM categories")
    abstract fun getAll(): Flowable<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE id = :id")
    abstract fun getById(id: Long): Flowable<CategoryDbModel>

    @Query("SELECT * FROM categories WHERE parentId = :id")
    abstract fun getByParentId(id: Long): Flowable<List<CategoryDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(category: CategoryDbModel): Completable

    @Update
    abstract fun update(category: CategoryDbModel): Completable

    @Query("DELETE FROM categories WHERE id=:id")
    abstract fun delete(id: Long): Completable

}
