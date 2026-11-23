package com.d9tilov.android.budget.domain.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import com.d9tilov.android.budget.domain.model.BudgetData
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class BudgetInteractorImplTest {
    private val budgetRepo: BudgetRepo = mockk(relaxed = true)
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)
    private val interactor: BudgetInteractor by lazy {
        BudgetInteractorImpl(budgetRepo, currencyInteractor)
    }

    private val testCurrency =
        CurrencyMetaData(
            clientId = "test-currency-client-id",
            code = "USD",
            symbol = "$",
        )

    private val testBudget =
        BudgetData(
            id = 1L,
            clientId = "test-client-id",
            sum = Decimal(1000),
            saveSum = Decimal(100),
            currencyCode = "USD",
            createdDate = LocalDateTime(2024, 1, 1, 0, 0),
        )

    @Before
    fun setup() {
        coEvery { currencyInteractor.getMainCurrency() } returns testCurrency
    }

    @Test
    fun `get should return existing budget from repo`() =
        runTest {
            coEvery { budgetRepo.get() } returns flowOf(testBudget)

            val budget = interactor.get().first()

            assertEquals(testBudget.sum, budget.sum)
            assertEquals(testBudget.saveSum, budget.saveSum)
            assertEquals(testBudget.currencyCode, budget.currencyCode)
        }

    @Test
    fun `get should create new budget if not exists`() =
        runTest {
            val newBudget =
                BudgetData(
                    id = 1L,
                    clientId = "new-client-id",
                    sum = Decimal.ZERO,
                    saveSum = Decimal.ZERO,
                    currencyCode = "USD",
                    createdDate = LocalDateTime(2024, 1, 1, 0, 0),
                )

            coEvery { budgetRepo.get() } returns flowOf(null)
            coEvery { budgetRepo.create("USD") } returns newBudget

            val budget = interactor.get().first()

            assertNotNull(budget)
            assertEquals("USD", budget.currencyCode)
            coVerify { budgetRepo.create("USD") }
        }

    @Test
    fun `create should create budget with main currency`() =
        runTest {
            val newBudget =
                BudgetData(
                    id = 1L,
                    clientId = "new-client-id",
                    sum = Decimal.ZERO,
                    saveSum = Decimal.ZERO,
                    currencyCode = "USD",
                    createdDate = LocalDateTime(2024, 1, 1, 0, 0),
                )

            coEvery { budgetRepo.create("USD") } returns newBudget

            val result = interactor.create()

            assertEquals("USD", result.currencyCode)
            coVerify { budgetRepo.create("USD") }
        }

    @Test
    fun `update should delegate to budgetRepo update`() =
        runTest {
            coEvery { budgetRepo.update(testBudget) } returns Unit

            interactor.update(testBudget)

            coVerify(exactly = 1) { budgetRepo.update(testBudget) }
        }

    @Test
    fun `updateBudgetWithCurrency should convert currency and update budget`() =
        runTest {
            coEvery { budgetRepo.get() } returns flowOf(testBudget)
            coEvery { currencyInteractor.toTargetCurrency(Decimal(1000), "USD", "EUR") } returns Decimal(850)
            coEvery { currencyInteractor.toTargetCurrency(Decimal(100), "USD", "EUR") } returns Decimal(85)
            coEvery { budgetRepo.update(any()) } returns Unit

            interactor.updateBudgetWithCurrency("EUR")

            coVerify {
                budgetRepo.update(
                    match {
                        it.currencyCode == "EUR" &&
                            it.sum == Decimal(850) &&
                            it.saveSum == Decimal(85)
                    },
                )
            }
        }

    @Test
    fun `updateBudgetWithCurrency should do nothing if budget is null`() =
        runTest {
            coEvery { budgetRepo.get() } returns flowOf(null)

            interactor.updateBudgetWithCurrency("EUR")

            coVerify(exactly = 0) { budgetRepo.update(any()) }
        }

    @Test
    fun `delete should delegate to budgetRepo delete`() =
        runTest {
            coEvery { budgetRepo.delete(testBudget) } returns Unit

            interactor.delete(testBudget)

            coVerify(exactly = 1) { budgetRepo.delete(testBudget) }
        }
}
