package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import com.d9tilov.moneymanager.category.data.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.category.exception.WrongCategorySetException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function3

class CategoryLocalSource(
    private val categoryMapper: CategoryMapper,
    private val prepopulateDataManager: PrepopulateDataManager,
    database: AppDatabase
) : ICategoryLocalSource {

    private val categoryDao = database.categoryDao()

    override fun createExpenseDefaultCategories(): Completable {
        val defaultCategories = prepopulateDataManager.createDefaultCategories()
        return Observable.fromIterable(defaultCategories)
            .flatMapCompletable { categoryDao.insert(categoryMapper.toDataModelFromPrePopulate(it)) }
    }

    override fun create(category: Category): Completable =
        try {
            val foundCategory = categoryDao.getByName(category.name).blockingFirst()
            Completable.error(CategoryExistException("Category with name: ${foundCategory.name} has already existed"))
        } catch (ex: Exception) {
            categoryDao.insert(categoryMapper.toDbModel(category))
        }

    override fun getById(id: Long): Flowable<Category> {
        val currentFlowable = categoryDao.getById(id)
        val parentFlowable = currentFlowable
            .flatMap { model -> categoryDao.getById(model.parentId) }
        val childrenFlowable = getByParentId(id)
        return Flowable.zip(
            currentFlowable, parentFlowable, childrenFlowable,
            Function3 { current, parent, children ->
                val category = categoryMapper.toDataModel(current, parent)
                if (current.parentId == NO_ID) {
                    category.copy(children = children)
                }
                category
            }
        )
    }

    override fun getByParentId(id: Long) =
        categoryDao.getByParentId(id).map { list: List<CategoryDbModel> ->
            list.map { categoryMapper.toDataParentModel(it) }
        }

    override fun getAllExpense() = categoryDao.getAllByType(CategoryType.EXPENSE)
        .map { groupChildrenWithParent(it) }

    override fun getAllIncome() = categoryDao.getAllByType(CategoryType.INCOME)
        .map { groupChildrenWithParent(it) }

    private fun groupChildrenWithParent(list: List<CategoryDbModel>): List<Category> {
        val childrenOfOneParent = list.groupBy { it.parentId }
        val parents = childrenOfOneParent[NO_ID]
            ?: throw WrongCategorySetException("Categories should contains at least ont parent category")
        val categories = mutableListOf<Category>()
        for (parent in parents) {
            val children = childrenOfOneParent[parent.id]
            if (children != null) {
                categories.add(
                    categoryMapper.toDataModel(parent)
                        .copy(
                            children = children.map {
                                categoryMapper.toDataModel(it)
                                    .copy(parent = categoryMapper.toDataModel(parent))
                            }
                        )
                )
            } else {
                categories.add(categoryMapper.toDataModel(parent))
            }
        }
        return categories
    }

    override fun update(category: Category) = categoryDao.update(categoryMapper.toDbModel(category))
    override fun delete(category: Category): Completable {
        return categoryDao.delete(category.id).andThen(
            Observable.fromIterable(category.children)
                .flatMapCompletable { child -> categoryDao.delete(child.id) }
        )
    }
}
