package com.d9tilov.moneymanager.category.data

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.local.CategorySource
import com.d9tilov.moneymanager.category.domain.CategoryRepo
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class CategoryRepoImpl(private val categoryLocalSource: CategorySource) :
    CategoryRepo {

    override fun createExpenseDefaultCategories() =
        categoryLocalSource.createExpenseDefaultCategories()

    override fun create(category: Category) = categoryLocalSource.create(category)
    override fun update(category: Category) = categoryLocalSource.update(category)

    override fun getCategoryById(id: Long): Maybe<Category> = categoryLocalSource.getById(id)

    override fun getCategoriesByType(type: TransactionType) =
        categoryLocalSource.getCategoriesByType(type)

    override fun getChildrenByParent(parentCategory: Category): Flowable<List<Category>> =
        categoryLocalSource.getByParentId(parentCategory.id)
            .flatMapSingle {
                Observable.fromIterable(it)
                    .map { subCategory -> subCategory.copy(parent = parentCategory) }
                    .toList()
            }

    override fun deleteCategory(category: Category): Completable =
        categoryLocalSource.delete(category)

    override fun deleteSubcategory(subCategory: Category): Single<Boolean> =
        categoryLocalSource.deleteSubcategory(subCategory)

    override fun deleteFromGroup(subCategory: Category): Single<Boolean> =
        categoryLocalSource.deleteFromGroup(subCategory)
}
