package com.d9tilov.moneymanager.category.data

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.local.CategorySource
import com.d9tilov.moneymanager.category.domain.CategoryRepo
import io.reactivex.Completable
import io.reactivex.Maybe

class CategoryDataRepo(private val categoryLocalSource: CategorySource) :
    CategoryRepo {

    override fun createExpenseDefaultCategories() =
        categoryLocalSource.createExpenseDefaultCategories()

    override fun create(category: Category) = categoryLocalSource.create(category)
    override fun update(category: Category) = categoryLocalSource.update(category)

    override fun getCategoryById(id: Long): Maybe<Category> = categoryLocalSource.getById(id)

    override fun getExpenseCategories() = categoryLocalSource.getAllExpense()
    override fun getChildrenByParent(parentCategory: Category) =
        categoryLocalSource.getByParentId(parentCategory.id)

    override fun deleteCategory(category: Category): Completable =
        categoryLocalSource.delete(category)
}
