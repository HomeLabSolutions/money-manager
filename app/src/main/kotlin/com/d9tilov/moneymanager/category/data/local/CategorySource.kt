package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategorySource : Source {

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