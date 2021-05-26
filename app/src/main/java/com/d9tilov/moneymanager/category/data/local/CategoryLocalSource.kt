package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongIdException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.data.local.entity.CategoryDbModel
import com.d9tilov.moneymanager.category.data.local.mapper.CategoryMapper
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.category.exception.NoCategoryParentException
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CategoryLocalSource(
    private val preferencesStore: PreferencesStore,
    private val categoryMapper: CategoryMapper,
    private val prepopulateDataManager: PrepopulateDataManager,
    private val categoryDao: CategoryDao
) : CategorySource {

    override suspend fun createExpenseDefaultCategories() {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val defaultCategories = prepopulateDataManager.createDefaultExpenseCategories()
            defaultCategories.forEach {
                categoryDao.create(
                    categoryMapper.toDataModelFromPrePopulate(
                        it, currentUserId
                    )
                )
            }
        }
    }

    override suspend fun createIncomeDefaultCategories() {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val defaultCategories = prepopulateDataManager.createDefaultIncomeCategories()
            defaultCategories.forEach {
                categoryDao.create(
                    categoryMapper.toDataModelFromPrePopulate(
                        it, currentUserId
                    )
                )
            }
        }
    }

    override suspend fun create(category: Category): Long {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val count = categoryDao.getCategoriesCountByName(currentUserId, category.name)
            if (count == 0) {
                categoryDao.create(categoryMapper.toDbModel(category.copy(clientId = currentUserId)))
            } else {
                throw CategoryExistException("Category with name: ${category.name} has already existed")
            }
        }
    }

    override suspend fun update(category: Category) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val categoryById =
                categoryDao.getById(currentUserId, category.id) ?: throw WrongIdException()
            if (categoryById.name == category.name) {
                categoryDao.update(categoryMapper.toDbModel(category))
            } else {
                val count = categoryDao.getCategoriesCountByName(currentUserId, category.name)
                if (count == 0) {
                    categoryDao.update(categoryMapper.toDbModel(category))
                } else {
                    throw CategoryExistException("Category with name: ${category.name} has already existed")
                }
            }
        }
    }

    override suspend fun getById(id: Long): Category {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val category = categoryDao.getById(currentUserId, id) ?: throw WrongIdException()
            val parentCategory =
                categoryDao.getById(currentUserId, category.parentId) ?: createDummyModel()
            val childrenCategories = getByParentId(id).first()
            val newCategory: Category = categoryMapper.toDataModel(
                category,
                if (parentCategory.id == NO_ID) null else parentCategory
            )
            return newCategory.copy(children = childrenCategories)
        }
    }

    private fun createDummyModel() = CategoryDbModel(
        NO_ID,
        "",
        NO_ID,
        TransactionType.EXPENSE,
        "",
        "",
        "",
        0
    )

    override fun getByParentId(id: Long): Flow<List<Category>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            categoryDao.getByParentId(currentUserId, id)
                .map { it.map { item -> categoryMapper.toDataParentModel(item) } }
        }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
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

    override suspend fun delete(category: Category) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            categoryDao.delete(currentUserId, category.id)
            category.children.forEach { categoryDao.delete(currentUserId, it.id) }
        }
    }

    override suspend fun deleteSubcategory(subCategory: Category): Boolean {
        val currentUserId = preferencesStore.uid
        return when {
            currentUserId == null -> {
                throw WrongUidException()
            }
            subCategory.parent == null -> throw NoCategoryParentException("Subcategory $subCategory has not parent")
            else -> {
                categoryDao.delete(currentUserId, subCategory.id)
                val list = categoryDao.getByParentId(
                    currentUserId,
                    subCategory.parent.id
                ).first()
                if (list.isEmpty()) {
                    categoryDao.delete(
                        currentUserId,
                        subCategory.parent.id
                    )
                }
                list.isEmpty()
            }
        }
    }

    override suspend fun deleteFromGroup(subCategory: Category): Boolean {
        val currentUserId = preferencesStore.uid
        return when {
            currentUserId == null -> {
                throw WrongUidException()
            }
            subCategory.parent == null -> {
                throw NoCategoryParentException("Subcategory $subCategory has not parent")
            }
            else -> {
                categoryDao.update(categoryMapper.toDbModel(subCategory.copy(parent = null)))
                val list = categoryDao.getByParentId(
                    currentUserId,
                    subCategory.parent.id
                ).first()
                if (list.isEmpty()) {
                    categoryDao.delete(
                        currentUserId,
                        subCategory.parent.id
                    )
                }
                list.isEmpty()
            }
        }
    }
}
