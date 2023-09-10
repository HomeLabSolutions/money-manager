package com.d9tilov.android.category.data.impl

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.category.data.contract.DefaultCategoriesManager
import com.d9tilov.android.category.data.impl.mapper.toDataModel
import com.d9tilov.android.category.data.impl.mapper.toDataModelFromPrePopulate
import com.d9tilov.android.category.data.impl.mapper.toDataParentModel
import com.d9tilov.android.category.data.impl.mapper.toDbModel
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.exception.CategoryException
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.exceptions.WrongIdException
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.database.dao.CategoryDao
import com.d9tilov.android.database.entity.CategoryDbModel
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryLocalSource @Inject constructor(
    @Dispatcher(MoneyManagerDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val preferencesStore: PreferencesStore,
    private val defaultCategoriesManager: DefaultCategoriesManager,
    private val categoryDao: CategoryDao
) : CategorySource {

    override suspend fun createExpenseDefaultCategories() = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.first()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val defaultCategories = defaultCategoriesManager.createDefaultExpenseCategories()
            defaultCategories.forEach {
                categoryDao.create(
                    it.toDataModelFromPrePopulate(
                        currentUserId
                    )
                )
            }
        }
    }

    override suspend fun createIncomeDefaultCategories() = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.first()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val defaultCategories = defaultCategoriesManager.createDefaultIncomeCategories()
            defaultCategories.forEach {
                categoryDao.create(it.toDataModelFromPrePopulate(currentUserId))
            }
        }
    }

    override suspend fun create(category: Category): Long = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.first()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val count = categoryDao.getCategoriesCountByName(currentUserId, category.name)
            if (count == 0) {
                categoryDao.create(category.copy(clientId = currentUserId).toDbModel())
            } else {
                throw CategoryException.CategoryExistException(
                    "Category with name: ${category.name} has already existed"
                )
            }
        }
    }

    override suspend fun update(category: Category) = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.first()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val categoryById =
                categoryDao.getById(currentUserId, category.id) ?: throw WrongIdException()
            if (categoryById.name == category.name) {
                categoryDao.update(category.toDbModel())
            } else {
                val count = categoryDao.getCategoriesCountByName(currentUserId, category.name)
                if (count == 0) {
                    categoryDao.update(category.toDbModel())
                } else {
                    throw CategoryException.CategoryExistException("Category with name: ${category.name} has already existed")
                }
            }
        }
    }

    override suspend fun getById(id: Long): Category = withContext(dispatcher) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val category = categoryDao.getById(currentUserId, id) ?: throw WrongIdException()
            val parentCategory =
                categoryDao.getById(currentUserId, category.parentId) ?: createDummyModel()
            val childrenCategories = getByParentId(id).first()
            val newCategory: Category =
                category.toDataModel(if (parentCategory.id == NO_ID) null else parentCategory)
            newCategory.copy(children = childrenCategories)
        }
    }

    private fun createDummyModel() = CategoryDbModel(
        NO_ID,
        "",
        NO_ID,
        TransactionType.EXPENSE.value,
        "",
        0,
        0,
        0
    )

    override fun getByParentId(id: Long): Flow<List<Category>> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                categoryDao.getByParentId(uid, id)
                    .map { it.map { item -> item.toDataParentModel() } }
            }
            .flowOn(dispatcher)

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                categoryDao.getAllByType(uid, type.value).map { groupChildrenWithParent(it) }
            }
            .flowOn(dispatcher)

    private fun groupChildrenWithParent(list: List<CategoryDbModel>): List<Category> {
        val childrenOfOneParent = list.groupBy { it.parentId }
        val parents = childrenOfOneParent[NO_ID] ?: emptyList()
        val categories = mutableListOf<Category>()
        for (parent in parents) {
            val children = childrenOfOneParent[parent.id]
            if (children != null) {
                categories.add(
                    parent.toDataModel()
                        .copy(
                            children = children.map {
                                it.toDataModel()
                                    .copy(parent = parent.toDataModel())
                            }
                        )
                )
            } else {
                categories.add(parent.toDataModel())
            }
        }
        return categories
    }

    override suspend fun delete(category: Category) = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.first()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            categoryDao.delete(currentUserId, category.id)
            category.children.forEach { categoryDao.delete(currentUserId, it.id) }
        }
    }

    override suspend fun deleteSubcategory(subCategory: Category): Boolean =
        withContext(dispatcher) {
            val currentUserId = preferencesStore.uid.first()
            when {
                currentUserId == null -> throw WrongUidException()
                subCategory.parent == null -> throw CategoryException.CategoryNoParentException("Subcategory $subCategory has not parent")
                else -> {
                    categoryDao.delete(currentUserId, subCategory.id)
                    val list = categoryDao.getByParentId(
                        currentUserId,
                        subCategory.parent?.id!!
                    ).first()
                    if (list.isEmpty()) categoryDao.delete(currentUserId, subCategory.parent?.id!!)
                    list.isEmpty()
                }
            }
        }

    override suspend fun deleteFromGroup(subCategory: Category): Boolean = withContext(dispatcher) {
        val currentUserId = preferencesStore.uid.first()
        when {
            currentUserId == null -> throw WrongUidException()
            subCategory.parent == null ->
                throw CategoryException.CategoryNoParentException("Subcategory $subCategory has not parent")

            else -> {
                categoryDao.update(subCategory.copy(parent = null).toDbModel())
                val list = categoryDao.getByParentId(
                    currentUserId,
                    subCategory.parent?.id!!
                ).first()
                if (list.isEmpty()) categoryDao.delete(currentUserId, subCategory.parent?.id!!)
                list.isEmpty()
            }
        }
    }
}
