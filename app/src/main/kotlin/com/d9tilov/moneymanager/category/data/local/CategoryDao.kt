package com.d9tilov.moneymanager.category.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.d9tilov.moneymanager.category.data.entity.CategoryDbModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE clientId=:uid AND type=:type")
    fun getAllByType(uid: String, type: TransactionType): Flow<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE clientId=:uid AND id=:id")
    suspend fun getById(uid: String, id: Long): CategoryDbModel?

    @Query("SELECT COUNT(*) FROM categories WHERE clientId=:uid AND name=:name")
    suspend fun getCategoriesCountByName(uid: String, name: String): Int

    @Query("SELECT * FROM categories WHERE clientId=:uid AND parentId=:id")
    fun getByParentId(uid: String, id: Long): Flow<List<CategoryDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(category: CategoryDbModel): Long

    @Update
    suspend fun update(category: CategoryDbModel)

    @Query("DELETE FROM categories WHERE clientId=:uid AND id=:id")
    suspend fun delete(uid: String, id: Long)
}
