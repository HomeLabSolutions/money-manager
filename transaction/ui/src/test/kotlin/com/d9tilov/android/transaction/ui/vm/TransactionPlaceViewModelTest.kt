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
    fun `editing expense uses its saved coordinates and only confirmation sets the name`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.locationData) }
            assertEquals(nearby, viewModel.uiState.value.placeState.places)
            assertEquals(null, viewModel.uiState.value.transaction.placeName)
            viewModel.onPlaceAction(TransactionPlaceAction.ACCEPT)
            viewModel.updatePlaceName("Cafe branch")
            viewModel.save()
            coVerify {
                transactions.update(
                    match {
                        it.placeName == "Cafe branch" &&
                            it.locationData == expense.locationData
                    },
                )
            }
        }

    @Test
    fun `income never searches or accepts a place`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(type = TransactionType.INCOME))
            val viewModel = createViewModel()
            runCurrent()
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            viewModel.updatePlaceName("Shop")
            viewModel.onPlaceAction(TransactionPlaceAction.SKIP)
            viewModel.onPlaceAction(TransactionPlaceAction.ACCEPT)
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
            assertEquals(null, viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `view mode neither searches nor modifies saved place`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(placeName = "Saved cafe"))
            val viewModel = createViewModel(TransactionInfoMode.VIEW)
            runCurrent()
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            viewModel.updatePlaceName("Changed")
            viewModel.onPlaceAction(TransactionPlaceAction.SKIP)
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
            assertEquals("Saved cafe", viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `without coordinates a custom name can still be saved`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns
                flowOf(expense.copy(locationData = LocationData.EMPTY))
            val viewModel = createViewModel()
            runCurrent()
            assertEquals(NearbyPlacesStatus.NO_LOCATION, viewModel.uiState.value.placeState.status)
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            viewModel.updatePlaceName("  My shop  ")
            viewModel.save()
            coVerify { transactions.update(match { it.placeName == "My shop" }) }
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
        }

    @Test
    fun `skip remains skipped when a pending lookup completes`() =
        runTest(dispatcher) {
            val response = CompletableDeferred<List<NearbyPlace>>()
            coEvery { places.getNearbyPlaces(any()) } coAnswers { response.await() }
            val viewModel = createViewModel()
            runCurrent()
            viewModel.onPlaceAction(TransactionPlaceAction.SKIP)
            response.complete(nearby)
            runCurrent()
            assertEquals("", viewModel.uiState.value.transaction.placeName)
            assertFalse(viewModel.uiState.value.placeState.isEditing)
            viewModel.save()
            coVerify { transactions.update(match { it.placeName == "" }) }
        }

    @Test
    fun `saved skip is not suggested again until edit is requested`() =
        runTest(dispatcher) {
            every { transactions.getTransactionById(TRANSACTION_ID) } returns flowOf(expense.copy(placeName = ""))
            val viewModel = createViewModel()
            runCurrent()
            coVerify(exactly = 0) { places.getNearbyPlaces(any()) }
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            runCurrent()
            coVerify(exactly = 1) { places.getNearbyPlaces(expense.locationData) }
        }

    @Test
    fun `saving custom text does not await lookup and later results preserve edits`() =
        runTest(dispatcher) {
            val response = CompletableDeferred<List<NearbyPlace>>()
            coEvery { places.getNearbyPlaces(any()) } coAnswers { response.await() }
            val viewModel = createViewModel()
            runCurrent()
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            viewModel.updatePlaceName("My place")
            viewModel.save()
            coVerify { transactions.update(match { it.placeName == "My place" }) }
            response.complete(nearby)
            runCurrent()
            assertEquals("My place", viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `service error supports manual entry and retry`() =
        runTest(dispatcher) {
            coEvery { places.getNearbyPlaces(any()) } throws IllegalStateException("Unavailable")
            val viewModel = createViewModel()
            runCurrent()
            assertEquals(NearbyPlacesStatus.UNAVAILABLE, viewModel.uiState.value.placeState.status)
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            viewModel.updatePlaceName("Custom")
            runCurrent()
            coEvery { places.getNearbyPlaces(any()) } returns nearby
            viewModel.onPlaceAction(TransactionPlaceAction.EDIT)
            runCurrent()
            assertEquals(NearbyPlacesStatus.READY, viewModel.uiState.value.placeState.status)
            assertEquals("Custom", viewModel.uiState.value.transaction.placeName)
        }

    @Test
    fun `later database emissions do not overwrite draft name`() =
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
    fun `view mode continues observing saved place changes`() =
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
