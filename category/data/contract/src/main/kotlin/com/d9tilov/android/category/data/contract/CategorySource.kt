package com.d9tilov.android.category.data.contract

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategorySource {
    suspend fun createExpenseDefaultCategories()

    suspend fun createIncomeDefaultCategories()

    suspend fun create(category: Category): Long

    suspend fun update(category: Category)

    suspend fun getById(id: Long): Category

    fun getByParentId(id: Long): Flow<List<Category>>

    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>

    suspend fun delete(category: Category)

    /**
     * Delete subcategory
     * @return true - if delete parent group also, false - otherwise
     */
    suspend fun deleteSubcategory(subCategory: Category): Boolean

    /**
     * Delete subcategory from group
     * @return true - if delete parent group also, false - otherwise
     */
    suspend fun deleteFromGroup(subCategory: Category): Boolean
}
