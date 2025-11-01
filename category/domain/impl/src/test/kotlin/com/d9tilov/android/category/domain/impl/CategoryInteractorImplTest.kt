package com.d9tilov.android.category.domain.impl

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.contract.CategoryRepo
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoryInteractorImplTest {
    private val categoryRepo: CategoryRepo = mockk(relaxed = true)
    private val interactor: CategoryInteractor by lazy { CategoryInteractorImpl(categoryRepo) }

    private val testCategory =
        Category(
            id = 1L,
            clientId = "test-category-client-id",
            name = "Test Category",
            type = TransactionType.EXPENSE,
            icon = 1,
            color = 1,
            usageCount = 0,
            children = emptyList(),
            parent = null,
        )

    private val testChildCategory =
        Category(
            id = 2L,
            clientId = "test-child-category-client-id",
            name = "Test Child Category",
            type = TransactionType.EXPENSE,
            icon = 1,
            color = 1,
            usageCount = 0,
            children = emptyList(),
            parent = testCategory,
        )

    private val testParentCategory =
        testCategory.copy(
            children = listOf(testChildCategory),
        )

    private val allItemsFolder =
        Category(
            id = -1L,
            clientId = "all-items-folder-client-id",
            name = "All Items",
            type = TransactionType.EXPENSE,
            icon = 0,
            color = 0,
            usageCount = 0,
            children = emptyList(),
            parent = null,
        )

    @Before
    fun setup() {
        every { categoryRepo.createAllItemsFolder(any()) } returns allItemsFolder
    }

    @Test
    fun `create should delegate to categoryRepo create`() =
        runTest {
            coEvery { categoryRepo.create(testCategory) } returns 1L

            val result = interactor.create(testCategory)

            assertEquals(1L, result)
            coVerify(exactly = 1) { categoryRepo.create(testCategory) }
        }

    @Test
    fun `createDefaultCategories should create both expense and income categories`() =
        runTest {
            coEvery { categoryRepo.createExpenseDefaultCategories() } returns Unit
            coEvery { categoryRepo.createIncomeDefaultCategories() } returns Unit

            interactor.createDefaultCategories()

            coVerify(exactly = 1) { categoryRepo.createExpenseDefaultCategories() }
            coVerify(exactly = 1) { categoryRepo.createIncomeDefaultCategories() }
        }

    @Test
    fun `update should delegate to categoryRepo update`() =
        runTest {
            coEvery { categoryRepo.update(testCategory) } returns Unit

            interactor.update(testCategory)

            coVerify(exactly = 1) { categoryRepo.update(testCategory) }
        }

    @Test
    fun `getCategoryById should return category from repo`() =
        runTest {
            coEvery { categoryRepo.getCategoryById(1L) } returns testCategory

            val result = interactor.getCategoryById(1L)

            assertEquals(testCategory.id, result.id)
            assertEquals(testCategory.name, result.name)
            coVerify(exactly = 1) { categoryRepo.getCategoryById(1L) }
        }

    @Test
    fun `getGroupedCategoriesByType should return flattened list with all items folder`() =
        runTest {
            val categories = listOf(testParentCategory)
            coEvery { categoryRepo.getCategoriesByType(TransactionType.EXPENSE) } returns flowOf(categories)

            val result = interactor.getGroupedCategoriesByType(TransactionType.EXPENSE).first()

            assertEquals(2, result.size)
            assertEquals(allItemsFolder.id, result.first().id)
            assertEquals(testChildCategory.id, result[1].id)
        }

    @Test
    fun `getAllCategoriesByType should return all categories from repo`() =
        runTest {
            val categories = listOf(testCategory)
            coEvery { categoryRepo.getCategoriesByType(TransactionType.EXPENSE) } returns flowOf(categories)

            val result = interactor.getAllCategoriesByType(TransactionType.EXPENSE).first()

            assertEquals(1, result.size)
            assertEquals(testCategory.id, result.first().id)
        }

    @Test
    fun `getChildrenByParent should return children from repo`() =
        runTest {
            val children = listOf(testChildCategory)
            coEvery { categoryRepo.getChildrenByParent(testParentCategory) } returns flowOf(children)

            val result = interactor.getChildrenByParent(testParentCategory).first()

            assertEquals(1, result.size)
            assertEquals(testChildCategory.id, result.first().id)
        }

    @Test
    fun `deleteCategory should delegate to categoryRepo deleteCategory`() =
        runTest {
            coEvery { categoryRepo.deleteCategory(testCategory) } returns Unit

            interactor.deleteCategory(testCategory)

            coVerify(exactly = 1) { categoryRepo.deleteCategory(testCategory) }
        }

    @Test
    fun `deleteSubCategory should return result from repo`() =
        runTest {
            coEvery { categoryRepo.deleteSubcategory(testChildCategory) } returns true

            val result = interactor.deleteSubCategory(testChildCategory)

            assertTrue(result)
            coVerify(exactly = 1) { categoryRepo.deleteSubcategory(testChildCategory) }
        }
}
