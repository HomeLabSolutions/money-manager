package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.entities.Category.Companion.ALL_ITEMS_ID
import io.reactivex.Flowable

class CategoryInteractor(private val categoryRepo: ICategoryRepo) :
    ICategoryInteractor {
    override fun createExpenseDefaultCategories() = categoryRepo.createExpenseDefaultCategories()
    override fun getExpenseCategories(): Flowable<List<Category>> {
        return categoryRepo.getExpenseCategories()
            .map { getOnlyChildrenWithAllItemsFolder(it) }
    }

    private fun getOnlyChildrenWithAllItemsFolder(categories: List<Category>): List<Category> {
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
        children = categories,
        type = CategoryType.EXPENSE,
        name = "",
        color = 0,
        icon = 0
    )
}
