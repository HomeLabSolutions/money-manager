package com.d9tilov.android.category.data.impl

import com.d9tilov.android.category.data.contract.CategoryRepo
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepoImpl(private val categoryLocalSource: com.d9tilov.android.category.data.contract.CategorySource) :
    CategoryRepo {

    override suspend fun createExpenseDefaultCategories() =
        categoryLocalSource.createExpenseDefaultCategories()

    override suspend fun createIncomeDefaultCategories() =
        categoryLocalSource.createIncomeDefaultCategories()

    override suspend fun create(category: Category) = categoryLocalSource.create(category)
    override suspend fun update(category: Category) = categoryLocalSource.update(category)

    override suspend fun getCategoryById(id: Long): Category = categoryLocalSource.getById(id)

    override fun getCategoriesByType(type: TransactionType) =
        categoryLocalSource.getCategoriesByType(type)

    override fun getChildrenByParent(parentCategory: Category): Flow<List<Category>> {
        return categoryLocalSource.getByParentId(parentCategory.id)
            .map { it.map { item -> item.copy(parent = parentCategory) } }
    }

    override suspend fun deleteCategory(category: Category) {
        categoryLocalSource.delete(category)
    }

    override suspend fun deleteSubcategory(subCategory: Category): Boolean =
        categoryLocalSource.deleteSubcategory(subCategory)

    override suspend fun deleteFromGroup(subCategory: Category): Boolean =
        categoryLocalSource.deleteFromGroup(subCategory)
}
