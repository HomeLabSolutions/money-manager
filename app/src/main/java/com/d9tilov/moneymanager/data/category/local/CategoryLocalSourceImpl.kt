package com.d9tilov.moneymanager.data.category.local

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.data.base.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.data.category.entities.Category
import com.d9tilov.moneymanager.data.category.local.entities.CategoryDbModel
import com.d9tilov.moneymanager.data.category.local.mappers.CategoryMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function3

class CategoryLocalSourceImpl(
    private val categoryMapper: CategoryMapper,
    private val prepopulateDataManager: PrepopulateDataManager,
    database: AppDatabase
) : CategoryLocalSource {

    private val categoryDao = database.categoryDao()

    override fun createDefaultCategories(): Completable {
        val defaultCategories = prepopulateDataManager.createDefaultCategories()
        return Observable.fromIterable(defaultCategories)
            .flatMapCompletable { categoryDao.insert(categoryMapper.toDataModelFromPrePopulate(it)) }
    }

    override fun create(category: Category) =
        categoryDao.insert(categoryMapper.toDbModel(category))

    override fun getById(id: Long): Flowable<Category> {
        val currentFlowable = categoryDao.getById(id)
        val parentFlowable = currentFlowable
            .flatMap { model -> categoryDao.getById(model.parentId) }
        val childrenFlowable = getByParentId(id)
        return Flowable.zip(currentFlowable, parentFlowable, childrenFlowable,
            Function3 { current, parent, children ->
                val category = categoryMapper.toDataModel(current, parent)
                if (current.parentId == NO_ID) {
                    category.copy(children = children)
                }
                category
            })
    }

    override fun getByParentId(id: Long) =
        categoryDao.getByParentId(id).map { list: List<CategoryDbModel> ->
            list.map { categoryMapper.toDataParentModel(it) }
        }

    override fun getAll(): Flowable<List<Category>> {
        return categoryDao.getAll()
            .map { list: List<CategoryDbModel> -> groupChildrenWithParent(list) }
    }

    private fun groupChildrenWithParent(list: List<CategoryDbModel>): List<Category> {
        val categories = mutableListOf<Category>()
        val childrenOfOneParent = list.groupBy { it.parentId }
        for (model in list) {
            if (model.parentId != NO_ID) {
                continue
            }
            categories.addAll(
                childrenOfOneParent[model.id]?.map { categoryMapper.toDataModel(it, model) }
                    ?: emptyList()
            )
        }
        return categories
    }


    override fun update(category: Category) = categoryDao.update(categoryMapper.toDbModel(category))
    override fun delete(category: Category) = categoryDao.update(categoryMapper.toDbModel(category))
}