package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.impl.mapper.toDataModel
import com.d9tilov.android.transaction.domain.model.Transaction
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionPlaceReadTest {
    @Test
    fun `lookup by id preserves saved place name and address with the other transaction fields`() =
        runTest {
            val repo: TransactionRepo = mockk()
            val categories: CategoryInteractor = mockk()
            val interactor =
                TransactionInteractorImpl(
                    transactionRepo = repo,
                    regularTransactionInteractor = mockk(),
                    categoryInteractor = categories,
                    userInteractor = mockk(),
                    currencyInteractor = mockk(),
                    budgetInteractor = mockk(),
                )
            val transaction = Transaction.EMPTY.copy(id = TRANSACTION_ID)
            coEvery { categories.getCategoryById(transaction.category.id) } returns transaction.category
            listOf(null to null, "Saved cafe" to null, "Saved cafe" to "High Street").forEach { (name, address) ->
                val saved = transaction.copy(place = transaction.place.copy(name = name, address = address))
                every { repo.getTransactionById(TRANSACTION_ID) } returns flowOf(saved.toDataModel())
                assertEquals(saved, interactor.getTransactionById(TRANSACTION_ID).first())
            }
        }

    private companion object {
        const val TRANSACTION_ID = 42L
    }
}
