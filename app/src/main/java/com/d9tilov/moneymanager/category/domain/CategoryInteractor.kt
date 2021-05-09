package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryInteractor : Interactor {

    suspend fun create(category: Category): Long
    suspend fun createDefaultCategories()
    suspend fun update(category: Category)
    suspend fun getCategoryById(id: Long): Category
    fun getGroupedCategoriesByType(type: TransactionType): Flow<List<Category>>
    fun getAllCategoriesByType(type: TransactionType): Flow<List<Category>>
    fun getChildrenByParent(parentCategory: Category): Flow<List<Category>>
    suspend fun deleteCategory(category: Category)
    suspend fun deleteSubCategory(subCategory: Category): Boolean
    suspend fun deleteFromGroup(subCategory: Category): Boolean
}
