package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.android.database.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {

    suspend fun createExpenseDefaultCategories()
    suspend fun createIncomeDefaultCategories()
    suspend fun create(category: Category): Long
    suspend fun update(category: Category)
    suspend fun getCategoryById(id: Long): Category
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    fun getChildrenByParent(parentCategory: Category): Flow<List<Category>>
    suspend fun deleteCategory(category: Category)
    suspend fun deleteSubcategory(subCategory: Category): Boolean
    suspend fun deleteFromGroup(subCategory: Category): Boolean
}
