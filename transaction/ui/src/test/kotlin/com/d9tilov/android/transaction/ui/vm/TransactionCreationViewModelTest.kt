package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.navigation.TRANSACTION_ID_ARG
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionCreationViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val transactionInteractor: TransactionInteractor = mockk(relaxed = true)
    private val categoryInteractor: CategoryInteractor = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { transactionInteractor.getTransactionById(TRANSACTION_ID) } returns flowOf(Transaction.EMPTY)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save becomes successful only after update completes`() =
        runTest(testDispatcher) {
            val allowUpdate = CompletableDeferred<Unit>()
            coEvery { transactionInteractor.update(any()) } coAnswers { allowUpdate.await() }
            val viewModel = createViewModel()
            runCurrent()

            val saveJob =
                launch {
                    viewModel.save()
                }
            runCurrent()

            assertFalse(saveJob.isCompleted)

            allowUpdate.complete(Unit)
            saveJob.join()
        }

    @Test
    fun `save exposes an error when update fails`() =
        runTest(testDispatcher) {
            coEvery { transactionInteractor.update(any()) } throws IllegalStateException("DB failure")
            val viewModel = createViewModel()
            runCurrent()

            val result = runCatching { viewModel.save() }

            assertEquals("DB failure", result.exceptionOrNull()?.message)
        }

    private fun createViewModel() =
        TransactionCreationViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TRANSACTION_ID_ARG to TRANSACTION_ID)),
            transactionInteractor = transactionInteractor,
            categoryInteractor = categoryInteractor,
        )

    private companion object {
        const val TRANSACTION_ID = 42L
    }
}
