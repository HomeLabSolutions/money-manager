package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.NearbyPlace
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionPlaceViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val transactions: TransactionInteractor = mockk(relaxed = true)
    private val places: NearbyPlacesRepo = mockk()
    private val expense = Transaction.EMPTY.copy(id = TRANSACTION_ID, locationData = LocationData(51.5, -0.1))
    private val nearby =
        listOf(NearbyPlace("cafe", "Cafe", "High Street", 10), NearbyPlace("shop", "Shop", "High Street", 25))

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        every { transactions.getTransactionById(TRANSACTION_ID) } returns flowOf(expense)
        coEvery { places.getNearbyPlaces(any()) } returns nearby
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `opening editor reads saved name without requesting places`() =
        runTest(dispatcher) {
            listOf(null, "", "Saved shop").forEach { name ->
                every { transactions.getTransactionById(TRANSACTION_ID) } returns flowOf(expense.copy(placeName = name))
                val viewModel = createViewModel()
                runCurrent()
                assertEquals(name, viewModel.uiState.value.transaction.placeName)
                assertEquals(NearbyPlacesStatus.IDLE, viewModel.uiState.value.placeState.status)
                assertFalse(viewModel.uiState.value.placeState.menuExpanded)
            }
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `pencil loads once while busy and opens list only after completion`() =
        runTest(dispatcher) {
            val response = CompletableDeferred<List<NearbyPlace>>()
            coEvery { places.getNearbyPlaces(any()) } coAnswers { response.await() }
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            viewModel.loadNearbyPlaces()
            runCurrent()
            assertEquals(NearbyPlacesStatus.LOADING, viewModel.uiState.value.placeState.status)
            assertFalse(viewModel.uiState.value.placeState.menuExpanded)
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.locationData) }
            response.complete(nearby)
            runCurrent()
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
            assertEquals(null, viewModel.uiState.value.transaction.placeName)
            assertTrue(viewModel.uiState.value.placeState.menuExpanded)
            coVerify(exactly = 0) { transactions.update(any()) }
        }

    @Test
    fun `each pencil tap after dismissal requests a fresh list`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            runCurrent()
            viewModel.dismissPlaceMenu()
            assertFalse(viewModel.uiState.value.placeState.menuExpanded)
            coEvery { places.getNearbyPlaces(any()) } returns nearby.reversed()
            viewModel.loadNearbyPlaces()
            runCurrent()
            assertEquals(nearby.reversed(), viewModel.uiState.value.placeState.places)
            coVerify(exactly = 2) { places.getNearbyPlaces(expense.locationData) }
        }

    @Test
    fun `saved name is preserved when new results arrive`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(placeName = "Saved shop"))
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            runCurrent()
            assertEquals("Saved shop", viewModel.uiState.value.transaction.placeName)
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
        }

    @Test
    fun `selected name closes menu and persists only with save`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            runCurrent()
            viewModel.updatePlaceName("Shop")
            assertFalse(viewModel.uiState.value.placeState.menuExpanded)
            coVerify(exactly = 0) { transactions.update(any()) }
            viewModel.save()
            coVerify {
                transactions.update(
                    match { it.placeName == "Shop" && it.locationData == expense.locationData },
                )
            }
        }

    @Test
    fun `reopening saved transaction restores name without another API request`() =
        runTest(dispatcher) {
            var stored = expense
            every { transactions.getTransactionById(TRANSACTION_ID) } answers { flowOf(stored) }
            coEvery { transactions.update(any()) } coAnswers { stored = firstArg() }
            val editor = createViewModel()
            runCurrent()
            editor.updatePlaceName("My cafe")
            editor.save()
            val reopened = createViewModel()
            runCurrent()
            assertEquals("My cafe", reopened.uiState.value.transaction.placeName)
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `without coordinates pencil opens manual naming without an API request`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(locationData = LocationData.EMPTY))
            val viewModel = createViewModel()
            runCurrent()
            assertEquals(NearbyPlacesStatus.IDLE, viewModel.uiState.value.placeState.status)
            viewModel.loadNearbyPlaces()
            assertEquals(NearbyPlacesStatus.NO_LOCATION, viewModel.uiState.value.placeState.status)
            assertTrue(viewModel.uiState.value.placeState.menuExpanded)
            viewModel.updatePlaceName("  My shop  ")
            viewModel.save()
            coVerify { transactions.update(match { it.placeName == "My shop" }) }
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `income and read only mode never request or modify places`() =
        runTest(dispatcher) {
            listOf(
                TransactionType.INCOME to TransactionInfoMode.EDIT,
                TransactionType.EXPENSE to TransactionInfoMode.VIEW,
            ).forEach { (type, mode) ->
                every { transactions.getTransactionById(TRANSACTION_ID) } returns flowOf(expense.copy(type = type))
                val viewModel = createViewModel(mode)
                runCurrent()
                viewModel.loadNearbyPlaces()
                viewModel.updatePlaceName("Changed")
                assertEquals(null, viewModel.uiState.value.transaction.placeName)
                assertFalse(viewModel.uiState.value.placeState.menuExpanded)
            }
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `empty list does not select a name`() =
        runTest(dispatcher) {
            coEvery { places.getNearbyPlaces(any()) } returns emptyList()
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            runCurrent()
            assertEquals(null, viewModel.uiState.value.transaction.placeName)
            assertTrue(viewModel.uiState.value.placeState.menuExpanded)
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
        }

    @Test
    fun `error ends loading and next pencil tap retries without losing name`() =
        runTest(dispatcher) {
            coEvery { places.getNearbyPlaces(any()) } throws IllegalStateException("Unavailable")
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            runCurrent()
            assertEquals(NearbyPlacesStatus.UNAVAILABLE, viewModel.uiState.value.placeState.status)
            assertTrue(viewModel.uiState.value.placeState.menuExpanded)
            viewModel.updatePlaceName("Custom")
            coEvery { places.getNearbyPlaces(any()) } returns nearby
            viewModel.loadNearbyPlaces()
            runCurrent()
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
            assertEquals("Custom", viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `saving does not wait for a pending list and results never overwrite draft`() =
        runTest(dispatcher) {
            val response = CompletableDeferred<List<NearbyPlace>>()
            coEvery { places.getNearbyPlaces(any()) } coAnswers { response.await() }
            val viewModel = createViewModel()
            runCurrent()
            viewModel.loadNearbyPlaces()
            runCurrent()
            viewModel.updatePlaceName("My place")
            viewModel.save()
            coVerify { transactions.update(match { it.placeName == "My place" }) }
            response.complete(nearby)
            runCurrent()
            assertEquals("My place", viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `later database emissions do not overwrite editing draft`() =
        runTest(dispatcher) {
            val updates = MutableStateFlow(expense)
            every { transactions.getTransactionById(TRANSACTION_ID) } returns updates
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlaceName("Draft")
            updates.value = expense.copy(description = "External update")
            runCurrent()
            assertEquals("Draft", viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `read only mode continues observing database name changes`() =
        runTest(dispatcher) {
            val updates = MutableStateFlow(expense)
            every { transactions.getTransactionById(TRANSACTION_ID) } returns updates
            val viewModel = createViewModel(TransactionInfoMode.VIEW)
            runCurrent()
            updates.value = expense.copy(placeName = "Updated cafe")
            runCurrent()
            assertEquals("Updated cafe", viewModel.uiState.value.transaction.placeName)
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    private fun createViewModel(mode: TransactionInfoMode = TransactionInfoMode.EDIT) =
        TransactionInfoViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        TRANSACTION_ID_ARG to TRANSACTION_ID,
                        TRANSACTION_MODE_ARG to mode.name,
                    ),
                ),
            transactionInteractor = transactions,
            categoryInteractor = mockk(relaxed = true),
            nearbyPlacesRepo = places,
        )

    private companion object {
        const val TRANSACTION_ID = 42L
    }
}
