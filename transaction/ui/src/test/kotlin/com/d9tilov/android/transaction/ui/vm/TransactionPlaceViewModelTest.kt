package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.NearbyPlace
import com.d9tilov.android.places.domain.model.Place
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
    private val places: NearbyPlacesInteractor = mockk()
    private val expense =
        Transaction.EMPTY.copy(
            id = TRANSACTION_ID,
            place = Place(location = LocationData(51.5, -0.1)),
        )
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
                every { transactions.getTransactionById(TRANSACTION_ID) } returns
                    flowOf(expense.copy(place = expense.place.copy(name = name)))
                val viewModel = createViewModel()
                runCurrent()
                assertEquals(name, viewModel.uiState.value.transaction.place.name)
                assertEquals(NearbyPlacesStatus.IDLE, viewModel.uiState.value.placeState.status)
                assertFalse(viewModel.uiState.value.placeState.pickerVisible)
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
            viewModel.updatePlacePickerVisibility(true)
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals(NearbyPlacesStatus.LOADING, viewModel.uiState.value.placeState.status)
            assertFalse(viewModel.uiState.value.placeState.pickerVisible)
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.place.location) }
            response.complete(nearby)
            runCurrent()
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
            assertEquals(null, viewModel.uiState.value.transaction.place.name)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            coVerify(exactly = 0) { transactions.update(any()) }
        }

    @Test
    fun `pencil reopens cached list after dismissal or selecting a name`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            viewModel.updatePlacePickerVisibility(false)
            assertFalse(viewModel.uiState.value.placeState.pickerVisible)
            viewModel.updatePlacePickerVisibility(true)
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
            viewModel.updatePlaceName("My custom place")
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals("My custom place", viewModel.uiState.value.transaction.place.name)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.place.location) }
        }

    @Test
    fun `a new view model starts without cached places`() =
        runTest(dispatcher) {
            val firstEditor = createViewModel()
            runCurrent()
            firstEditor.updatePlacePickerVisibility(true)
            runCurrent()
            val secondEditor = createViewModel()
            runCurrent()
            assertEquals(NearbyPlacesStatus.IDLE, secondEditor.uiState.value.placeState.status)
            assertTrue(
                secondEditor.uiState.value.placeState.places
                    .isEmpty(),
            )
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.place.location) }
            coEvery { places.getNearbyPlaces(any()) } returns nearby.reversed()
            secondEditor.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals(nearby.reversed(), secondEditor.uiState.value.placeState.places)
            coVerify(exactly = 2) { places.getNearbyPlaces(expense.place.location) }
        }

    @Test
    fun `saved name is preserved when new results arrive`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(place = expense.place.copy(name = "Saved shop")))
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals("Saved shop", viewModel.uiState.value.transaction.place.name)
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
        }

    @Test
    fun `selected name and address close picker and persist only with save`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            viewModel.selectPlace(nearby[1])
            assertEquals("High Street", viewModel.uiState.value.transaction.place.address)
            assertFalse(viewModel.uiState.value.placeState.pickerVisible)
            coVerify(exactly = 0) { transactions.update(any()) }
            viewModel.save()
            coVerify {
                transactions.update(
                    match {
                        it.place.name == "Shop" && it.place.address == "High Street" &&
                            it.place.location == expense.place.location
                    },
                )
            }
        }

    @Test
    fun `reopening saved transaction restores name and address without another API request`() =
        runTest(dispatcher) {
            var stored = expense
            every { transactions.getTransactionById(TRANSACTION_ID) } answers { flowOf(stored) }
            coEvery { transactions.update(any()) } coAnswers { stored = firstArg() }
            val editor = createViewModel()
            runCurrent()
            editor.selectPlace(nearby.first().copy(name = "My cafe", address = "  High Street  "))
            editor.save()
            val reopened = createViewModel()
            runCurrent()
            assertEquals("My cafe", reopened.uiState.value.transaction.place.name)
            assertEquals("High Street", reopened.uiState.value.transaction.place.address)
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `changing places replaces address and custom naming clears old address`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            viewModel.selectPlace(nearby.first())
            viewModel.selectPlace(nearby[1].copy(address = "Market Street"))
            assertEquals("Market Street", viewModel.uiState.value.transaction.place.address)
            val selectedPlace = viewModel.uiState.value.transaction.place
            listOf(null, "", "   ").forEach { name ->
                viewModel.updatePlaceName(name)
                assertEquals(selectedPlace, viewModel.uiState.value.transaction.place)
            }
            viewModel.updatePlaceName("My own place")
            assertEquals(null, viewModel.uiState.value.transaction.place.address)
            viewModel.selectPlace(nearby[1].copy(address = ""))
            assertEquals(null, viewModel.uiState.value.transaction.place.address)
        }

    @Test
    fun `without coordinates pencil opens manual naming without an API request`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(place = expense.place.copy(location = LocationData.EMPTY)))
            val viewModel = createViewModel()
            runCurrent()
            assertEquals(NearbyPlacesStatus.IDLE, viewModel.uiState.value.placeState.status)
            viewModel.updatePlacePickerVisibility(true)
            assertEquals(NearbyPlacesStatus.NO_LOCATION, viewModel.uiState.value.placeState.status)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            viewModel.updatePlaceName("  My shop  ")
            viewModel.save()
            coVerify { transactions.update(match { it.place.name == "My shop" }) }
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
                viewModel.updatePlacePickerVisibility(true)
                viewModel.updatePlaceName("Changed")
                viewModel.selectPlace(nearby.first())
                assertEquals(null, viewModel.uiState.value.transaction.place.name)
                assertFalse(viewModel.uiState.value.placeState.pickerVisible)
            }
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `empty list is cached without selecting a name`() =
        runTest(dispatcher) {
            coEvery { places.getNearbyPlaces(any()) } returns emptyList()
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals(null, viewModel.uiState.value.transaction.place.name)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
            viewModel.updatePlacePickerVisibility(false)
            viewModel.updatePlacePickerVisibility(true)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
            runCurrent()
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.place.location) }
        }

    @Test
    fun `error ends loading and next pencil tap retries without losing name`() =
        runTest(dispatcher) {
            coEvery { places.getNearbyPlaces(any()) } throws IllegalStateException("Unavailable")
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals(NearbyPlacesStatus.UNAVAILABLE, viewModel.uiState.value.placeState.status)
            assertTrue(viewModel.uiState.value.placeState.pickerVisible)
            viewModel.updatePlaceName("Custom")
            coEvery { places.getNearbyPlaces(any()) } returns nearby
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
            assertEquals("Custom", viewModel.uiState.value.transaction.place.name)
        }

    @Test
    fun `saving does not wait for a pending list and results never overwrite draft`() =
        runTest(dispatcher) {
            val response = CompletableDeferred<List<NearbyPlace>>()
            coEvery { places.getNearbyPlaces(any()) } coAnswers { response.await() }
            val viewModel = createViewModel()
            runCurrent()
            viewModel.updatePlacePickerVisibility(true)
            runCurrent()
            viewModel.updatePlaceName("My place")
            viewModel.save()
            coVerify { transactions.update(match { it.place.name == "My place" }) }
            response.complete(nearby)
            runCurrent()
            assertEquals("My place", viewModel.uiState.value.transaction.place.name)
        }

    @Test
    fun `editing mode continues observing database changes`() =
        runTest(dispatcher) {
            val updates = MutableStateFlow(expense)
            every { transactions.getTransactionById(TRANSACTION_ID) } returns updates
            val viewModel = createViewModel()
            runCurrent()
            val updatedTransaction =
                expense.copy(
                    description = "External update",
                    place = expense.place.copy(name = "Updated cafe"),
                )
            updates.value = updatedTransaction
            runCurrent()
            assertEquals(updatedTransaction, viewModel.uiState.value.transaction)
        }

    @Test
    fun `read only mode continues observing database name changes`() =
        runTest(dispatcher) {
            val updates = MutableStateFlow(expense)
            every { transactions.getTransactionById(TRANSACTION_ID) } returns updates
            val viewModel = createViewModel(TransactionInfoMode.VIEW)
            runCurrent()
            updates.value = expense.copy(place = expense.place.copy(name = "Updated cafe"))
            runCurrent()
            assertEquals("Updated cafe", viewModel.uiState.value.transaction.place.name)
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
            nearbyPlacesInteractor = places,
        )

    private companion object {
        const val TRANSACTION_ID = 42L
    }
}
