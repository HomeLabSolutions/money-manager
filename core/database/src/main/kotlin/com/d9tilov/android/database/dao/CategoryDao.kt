package com.d9tilov.android.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.d9tilov.android.database.entity.CategoryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE clientId=:uid AND type=:type")
    fun getAllByType(
        uid: String,
        type: Int,
    ): Flow<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE clientId=:uid AND id=:id")
    suspend fun getById(
        uid: String,
        id: Long,
    ): CategoryDbModel?

    @Query("SELECT COUNT(*) FROM categories WHERE clientId=:uid AND name=:name")
    suspend fun getCategoriesCountByName(
        uid: String,
        name: String,
    ): Int

    @Query("SELECT * FROM categories WHERE clientId=:uid AND parentId=:id")
    fun getByParentId(
        uid: String,
        id: Long,
    ): Flow<List<CategoryDbModel>>

    @Upsert
    suspend fun create(category: CategoryDbModel): Long

    @Update
    suspend fun update(category: CategoryDbModel)

    @Query("DELETE FROM categories WHERE clientId=:uid AND id=:id")
    suspend fun delete(
        uid: String,
        id: Long,
    )
}
