package com.d9tilov.android.category.domain.contract

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
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

    fun createAllItemsFolder(categories: List<Category>): Category
}
