package com.d9tilov.android.category.domain.impl

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.contract.CategoryRepo
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoryInteractorImpl(
    private val categoryRepo: CategoryRepo,
) : CategoryInteractor {
    override suspend fun create(category: Category) = categoryRepo.create(category)

    override suspend fun createDefaultCategories() {
        coroutineScope {
            launch { categoryRepo.createExpenseDefaultCategories() }
            launch { categoryRepo.createIncomeDefaultCategories() }
        }
    }

    override suspend fun update(category: Category) = categoryRepo.update(category)

    override suspend fun getCategoryById(id: Long) = categoryRepo.getCategoryById(id)

    override fun getGroupedCategoriesByType(type: TransactionType): Flow<List<Category>> =
        categoryRepo.getCategoriesByType(type).map {
            getAllWithChildrenInSingleList(it)
        }

    override fun getAllCategoriesByType(type: TransactionType): Flow<List<Category>> =
        categoryRepo.getCategoriesByType(type)

    override fun getChildrenByParent(parentCategory: Category): Flow<List<Category>> =
        categoryRepo.getChildrenByParent(parentCategory)

    override suspend fun deleteCategory(category: Category) {
        categoryRepo.deleteCategory(category)
    }

    override suspend fun deleteSubCategory(subCategory: Category): Boolean = categoryRepo.deleteSubcategory(subCategory)

    override suspend fun deleteFromGroup(subCategory: Category): Boolean = categoryRepo.deleteFromGroup(subCategory)

    private fun getAllWithChildrenInSingleList(categories: List<Category>): List<Category> {
        val categoriesAsChild = mutableListOf<Category>()
        for (category in categories) {
            if (category.children.isNotEmpty()) {
                categoriesAsChild.addAll(category.children)
            } else {
                categoriesAsChild.add(category)
            }
        }
        categoriesAsChild.add(0, categoryRepo.createAllItemsFolder(categories))
        return categoriesAsChild
    }
}
