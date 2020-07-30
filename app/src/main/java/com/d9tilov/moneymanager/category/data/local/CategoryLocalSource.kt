package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import com.d9tilov.moneymanager.category.data.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.functions.Function3

class CategoryLocalSource(
    private val preferencesStore: PreferencesStore,
    private val categoryMapper: CategoryMapper,
    private val prepopulateDataManager: PrepopulateDataManager,
    database: AppDatabase
) : CategorySource {

    private val categoryDao = database.categoryDao()

    override fun createExpenseDefaultCategories(): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            val defaultCategories = prepopulateDataManager.createDefaultCategories(currentUserId)
            Observable.fromIterable(defaultCategories)
                .flatMapCompletable {
                    categoryDao.create(
                        categoryMapper.toDataModelFromPrePopulate(
                            it
                        )
                    )
                }
        }
    }

    override fun create(category: Category): Completable =
        try {
            val currentUserId = preferencesStore.uid
            if (currentUserId == null) {
                Completable.error(WrongUidException())
            } else {
                val foundCategory =
                    categoryDao.getByName(currentUserId, category.name).blockingFirst()
                Completable.error(CategoryExistException("Category with name: ${foundCategory.name} has already existed"))
            }
        } catch (ex: Exception) {
            categoryDao.create(categoryMapper.toDbModel(category))
        }

    override fun update(category: Category) = categoryDao.update(categoryMapper.toDbModel(category))
    override fun getById(id: Long): Maybe<Category> {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            return Maybe.error(WrongUidException())
        } else {
            val currentFlowable = categoryDao.getById(currentUserId, id)
            val parentFlowable = currentFlowable
                .flatMap { model ->
                    categoryDao.getById(currentUserId, model.parentId)
                        .defaultIfEmpty(createDummyModel())
                }
            val childrenFlowable = getByParentId(id)
                .defaultIfEmpty(emptyList())
            return Maybe.zip(
                currentFlowable, parentFlowable, childrenFlowable,
                Function3 { current, parent, children ->
                    val category = categoryMapper.toDataModel(
                        current,
                        if (parent.id == NO_ID) null else parent
                    )
                    category.copy(children = children)
                    category
                }
            )
        }
    }

    private fun createDummyModel() = CategoryDbModel(
        NO_ID,
        "",
        NO_ID,
        CategoryType.EXPENSE,
        "",
        "",
        "",
        0,
        0
    )

    override fun getByParentId(id: Long): Maybe<List<Category>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Maybe.error(WrongUidException())
        } else {
            categoryDao.getByParentId(currentUserId, id).map { list: List<CategoryDbModel> ->
                list.map { categoryMapper.toDataParentModel(it) }
            }
        }
    }

    override fun getCategoriesByType(type: CategoryType): Flowable<List<Category>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            categoryDao.getAllByType(currentUserId, type)
                .map { groupChildrenWithParent(it) }
        }
    }

    private fun groupChildrenWithParent(list: List<CategoryDbModel>): List<Category> {
        val childrenOfOneParent = list.groupBy { it.parentId }
        val parents = childrenOfOneParent[NO_ID]
            ?: emptyList()
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

    override fun delete(category: Category): Completable {
        return categoryDao.delete(category.id).andThen(
            Observable.fromIterable(category.children)
                .flatMapCompletable { child -> categoryDao.delete(child.id) }
        )
    }
}
