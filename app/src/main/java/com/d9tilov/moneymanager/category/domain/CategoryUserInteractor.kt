package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.entities.Category.Companion.ALL_ITEMS_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class CategoryUserInteractor(private val categoryRepo: CategoryRepo) :
    CategoryInteractor {
    override fun createExpenseDefaultCategories() = categoryRepo.createExpenseDefaultCategories()
    override fun create(category: Category) = categoryRepo.create(category)
    override fun update(category: Category) = categoryRepo.update(category)

    override fun getCategoryById(id: Long) = categoryRepo.getCategoryById(id)

    override fun getGroupedCategoriesByType(type: CategoryType): Flowable<List<Category>> {
        return categoryRepo.getCategoriesByType(type)
            .map { getAllWithChildrenInSingleList(it) }
    }

    override fun getAllCategoriesByType(type: CategoryType): Flowable<List<Category>> = categoryRepo.getCategoriesByType(type)
    override fun getChildrenByParent(parentCategory: Category): Maybe<List<Category>> =
        categoryRepo.getChildrenByParent(parentCategory)

    override fun deleteCategory(category: Category): Completable =
        categoryRepo.deleteCategory(category)

    private fun getAllWithChildrenInSingleList(categories: List<Category>): List<Category> {
        val categoriesAsChild = mutableListOf<Category>()
        for (category in categories) {
            if (category.children.isNotEmpty()) {
                categoriesAsChild.addAll(category.children)
            } else {
                categoriesAsChild.add(category)
            }
        }
        categoriesAsChild.add(0, createAllItemsFolder(categories))
        return categoriesAsChild
    }

    private fun createAllItemsFolder(categories: List<Category>) = Category(
        id = ALL_ITEMS_ID,
        clientId = NO_ID.toString(),
        children = categories,
        type = CategoryType.EXPENSE,
        name = "",
        color = 0,
        icon = 0,
        priority = -1,
        ordinal = -1
    )
}
