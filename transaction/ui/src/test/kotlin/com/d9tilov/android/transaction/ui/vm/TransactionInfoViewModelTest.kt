package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.navigation.TRANSACTION_ID_ARG
import com.d9tilov.android.transaction.ui.navigation.TRANSACTION_MODE_ARG
import io.mockk.coEvery
import io.mockk.coVerify
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
class TransactionInfoViewModelTest {
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
            val viewModel = createViewModel(mode = TransactionInfoMode.EDIT)
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
            val viewModel = createViewModel(mode = TransactionInfoMode.EDIT)
            runCurrent()

            val result = runCatching { viewModel.save() }

            assertEquals("DB failure", result.exceptionOrNull()?.message)
        }

    @Test
    fun `view mode loads transaction but never saves changes`() =
        runTest(testDispatcher) {
            val transaction = Transaction.EMPTY.copy(id = TRANSACTION_ID, description = "Original")
            every { transactionInteractor.getTransactionById(TRANSACTION_ID) } returns flowOf(transaction)
            val viewModel = createViewModel(mode = TransactionInfoMode.VIEW)
            assertEquals(TransactionInfoMode.VIEW, viewModel.uiState.value.mode)
            viewModel.save()
            coVerify(exactly = 0) { transactionInteractor.update(any()) }
            runCurrent()

            assertEquals(TransactionInfoMode.VIEW, viewModel.uiState.value.mode)
            assertEquals(transaction, viewModel.uiState.value.transaction)
            viewModel.save()
            coVerify(exactly = 0) { transactionInteractor.update(any()) }
        }

    @Test
    fun `explicit edit mode allows saving changes`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel(mode = TransactionInfoMode.EDIT)
            assertEquals(TransactionInfoMode.VIEW, viewModel.uiState.value.mode)
            viewModel.save()
            coVerify(exactly = 0) { transactionInteractor.update(any()) }
            runCurrent()
            assertEquals(TransactionInfoMode.EDIT, viewModel.uiState.value.mode)
            viewModel.updateDescription("Edited")
            viewModel.save()
            coVerify { transactionInteractor.update(match { it.description == "Edited" }) }
        }

    @Test(expected = IllegalStateException::class)
    fun `navigation mode is required`() {
        TransactionInfoViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TRANSACTION_ID_ARG to TRANSACTION_ID)),
            transactionInteractor = transactionInteractor,
            categoryInteractor = categoryInteractor,
        )
    }

    private fun createViewModel(mode: TransactionInfoMode) =
        TransactionInfoViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(TRANSACTION_ID_ARG to TRANSACTION_ID, TRANSACTION_MODE_ARG to mode.name),
                ),
            transactionInteractor = transactionInteractor,
            categoryInteractor = categoryInteractor,
        )

    private companion object {
        const val TRANSACTION_ID = 42L
    }
}
