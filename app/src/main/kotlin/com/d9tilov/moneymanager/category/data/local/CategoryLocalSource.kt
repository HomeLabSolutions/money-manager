package com.d9tilov.moneymanager.category.data.local

import android.util.Log
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongIdException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.data.entity.CategoryDbModel
import com.d9tilov.moneymanager.category.data.local.mapper.CategoryMapper
import com.d9tilov.moneymanager.category.exception.CategoryException
import com.d9tilov.moneymanager.core.constants.DataConstants.NO_ID
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryLocalSource(
    private val preferencesStore: PreferencesStore,
    private val categoryMapper: CategoryMapper,
    private val prepopulateDataManager: PrepopulateDataManager,
    private val categoryDao: CategoryDao
) : CategorySource {

    private val categories = MutableStateFlow(setOf<Category>())

    override suspend fun saveInMemory(categories: List<Category>) {
        Log.d("moggot", "saveInMemory")
        this.categories.value = categories.toSet()
    }

    override suspend fun createExpenseDefaultCategories() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
            val defaultCategories = prepopulateDataManager.createDefaultExpenseCategories()
            defaultCategories.forEach {
                categoryDao.create(
                    categoryMapper.toDataModelFromPrePopulate(
                        it,
                        currentUserId
                    )
                )
            }
        }
    }

    override suspend fun createIncomeDefaultCategories() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
            val defaultCategories = prepopulateDataManager.createDefaultIncomeCategories()
            defaultCategories.forEach {
                categoryDao.create(categoryMapper.toDataModelFromPrePopulate(it, currentUserId))
            }
        }
    }

    override suspend fun create(category: Category): Long {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else {
            val count = categoryDao.getCategoriesCountByName(currentUserId, category.name)
            if (count == 0) {
                categoryDao.create(categoryMapper.toDbModel(category.copy(clientId = currentUserId)))
            } else throw CategoryException.CategoryExistException("Category with name: ${category.name} has already existed")
        }
    }

    override suspend fun update(category: Category) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
            val categoryById =
                categoryDao.getById(currentUserId, category.id) ?: throw WrongIdException()
            if (categoryById.name == category.name) {
                categoryDao.update(categoryMapper.toDbModel(category))
            } else {
                val count = categoryDao.getCategoriesCountByName(currentUserId, category.name)
                if (count == 0) categoryDao.update(categoryMapper.toDbModel(category))
                else throw CategoryException.CategoryExistException("Category with name: ${category.name} has already existed")
            }
        }
    }

    override suspend fun getById(id: Long): Category {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
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
        0,
        0,
        0
    )

    override fun getByParentId(id: Long): Flow<List<Category>> {
        return preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                categoryDao.getByParentId(uid, id)
                    .map { it.map { item -> categoryMapper.toDataParentModel(item) } }
            }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return if (categories.value.isEmpty()) {
            Log.d("moggot", "getCategoriesByType empty")
            preferencesStore.uid
                .filterNotNull()
                .flatMapMerge { uid -> categoryDao.getAll(uid).map { groupChildrenWithParent(it) } }
                .flowOn(Dispatchers.IO)
        } else {
            Log.d("moggot", "getCategoriesByType non empty")
            categories.map { it.filter { it.type == type } }
        }
    }

    private fun groupChildrenWithParent(list: List<CategoryDbModel>): List<Category> {
        val childrenOfOneParent = list.groupBy { it.parentId }
        val parents = childrenOfOneParent[NO_ID] ?: emptyList()
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
            } else categories.add(categoryMapper.toDataModel(parent))
        }
        return categories
    }

    override suspend fun delete(category: Category) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else {
            categoryDao.delete(currentUserId, category.id)
            category.children.forEach { categoryDao.delete(currentUserId, it.id) }
        }
    }

    override suspend fun deleteSubcategory(subCategory: Category): Boolean {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return when {
            currentUserId == null -> throw WrongUidException()
            subCategory.parent == null -> throw CategoryException.CategoryNoParentException("Subcategory $subCategory has not parent")
            else -> {
                delete(subCategory)
                val list = categoryDao.getByParentId(
                    currentUserId,
                    subCategory.parent.id
                ).firstOrNull() ?: emptyList()
                if (list.isEmpty()) delete(subCategory.parent)
                list.isEmpty()
            }
        }
    }

    override suspend fun deleteFromGroup(subCategory: Category): Boolean {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return when {
            currentUserId == null -> throw WrongUidException()
            subCategory.parent == null ->
                throw CategoryException.CategoryNoParentException("Subcategory $subCategory has not parent")
            else -> {
                update(subCategory.copy(parent = null))
                val list = categoryDao.getByParentId(
                    currentUserId,
                    subCategory.parent.id
                ).firstOrNull() ?: emptyList()
                if (list.isEmpty()) delete(subCategory.parent)
                list.isEmpty()
            }
        }
    }
}
