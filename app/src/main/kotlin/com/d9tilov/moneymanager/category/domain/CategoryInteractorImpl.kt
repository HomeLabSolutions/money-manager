package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.data.entity.Category.Companion.ALL_ITEMS_ID
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.database.model.TransactionType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoryInteractorImpl(private val categoryRepo: CategoryRepo) :
    CategoryInteractor {

    override suspend fun create(category: Category) = categoryRepo.create(category)
    override suspend fun createDefaultCategories() {
        coroutineScope {
            launch { categoryRepo.createExpenseDefaultCategories() }
            launch { categoryRepo.createIncomeDefaultCategories() }
        }
    }

    override suspend fun update(category: Category) = categoryRepo.update(category)

    override suspend fun getCategoryById(id: Long) = categoryRepo.getCategoryById(id)

    override fun getGroupedCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryRepo.getCategoriesByType(type).map { getAllWithChildrenInSingleList(it) }
    }

    override fun getAllCategoriesByType(type: TransactionType): Flow<List<Category>> =
        categoryRepo.getCategoriesByType(type)

    override fun getChildrenByParent(parentCategory: Category): Flow<List<Category>> =
        categoryRepo.getChildrenByParent(parentCategory)

    override suspend fun deleteCategory(category: Category) {
        categoryRepo.deleteCategory(category)
    }

    override suspend fun deleteSubCategory(subCategory: Category): Boolean =
        categoryRepo.deleteSubcategory(subCategory)

    override suspend fun deleteFromGroup(subCategory: Category): Boolean =
        categoryRepo.deleteFromGroup(subCategory)

    private fun getAllWithChildrenInSingleList(categories: List<Category>): List<Category> {
        val categoriesAsChild = mutableListOf<Category>()
        for (category in categories) {
            if (category.children.isNotEmpty()) categoriesAsChild.addAll(category.children)
            else categoriesAsChild.add(category)
        }
        categoriesAsChild.add(0, createAllItemsFolder(categories))
        return categoriesAsChild
    }

    private fun createAllItemsFolder(categories: List<Category>) = Category.EMPTY_EXPENSE.copy(
        id = ALL_ITEMS_ID,
        clientId = NO_ID.toString(),
        children = categories,
        type = TransactionType.EXPENSE,
        name = "",
        color = R.color.category_all_color,
        icon = R.drawable.ic_category_folder,
        usageCount = Integer.MAX_VALUE
    )
}
