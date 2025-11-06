package com.d9tilov.android.currency.ui

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val analyticsSender: AnalyticsSender = mockk(relaxed = true)
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)
    private val currencyUpdateObserver: CurrencyUpdateObserver = mockk(relaxed = true)

    private val testCurrencies =
        listOf(
            DomainCurrency(
                code = "USD",
                symbol = "$",
                isBase = false,
                value = BigDecimal.ONE,
                lastUpdateTime = 0L,
            ),
            DomainCurrency(
                code = "EUR",
                symbol = "€",
                isBase = false,
                value = BigDecimal("0.85"),
                lastUpdateTime = 0L,
            ),
            DomainCurrency(
                code = "GBP",
                symbol = "£",
                isBase = false,
                value = BigDecimal("0.73"),
                lastUpdateTime = 0L,
            ),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { currencyInteractor.getCurrencies() } returns flowOf(testCurrencies)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load currencies and update UI state`() =
        runTest {
            val savedStateHandle = SavedStateHandle()
            val viewModel =
                CurrencyViewModel(
                    testDispatcher,
                    savedStateHandle,
                    currencyInteractor,
                    analyticsSender,
                    currencyUpdateObserver,
                )
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is CurrencyUiState.HasCurrencies)
            if (state is CurrencyUiState.HasCurrencies) {
                assertEquals(3, state.currencyList.size)
                assertEquals(false, state.isLoading)
            }
        }

    @Test
    fun `init with selected currency should mark it as base`() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("currency_code_args" to "EUR"))
            val viewModel =
                CurrencyViewModel(
                    testDispatcher,
                    savedStateHandle,
                    currencyInteractor,
                    analyticsSender,
                    currencyUpdateObserver,
                )
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is CurrencyUiState.HasCurrencies)
            if (state is CurrencyUiState.HasCurrencies) {
                val eur = state.currencyList.find { it.code == "EUR" }
                assertEquals(true, eur?.isBase)

                val usd = state.currencyList.find { it.code == "USD" }
                assertEquals(false, usd?.isBase)
            }
        }

    @Test
    fun `changeCurrency should update main currency when no currency selected`() =
        runTest {
            val savedStateHandle = SavedStateHandle()
            coEvery { currencyUpdateObserver.updateMainCurrency(any()) } returns Unit

            val viewModel =
                CurrencyViewModel(
                    testDispatcher,
                    savedStateHandle,
                    currencyInteractor,
                    analyticsSender,
                    currencyUpdateObserver,
                )
            advanceUntilIdle()

            viewModel.changeCurrency("EUR")
            advanceUntilIdle()

            coVerify { currencyUpdateObserver.updateMainCurrency("EUR") }
        }

    @Test
    fun `changeCurrency should not update when currency is already selected`() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("currency_code_args" to "USD"))

            val viewModel =
                CurrencyViewModel(
                    testDispatcher,
                    savedStateHandle,
                    currencyInteractor,
                    analyticsSender,
                    currencyUpdateObserver,
                )
            advanceUntilIdle()

            viewModel.changeCurrency("EUR")
            advanceUntilIdle()

            coVerify(exactly = 0) { currencyUpdateObserver.updateMainCurrency(any()) }
        }

    @Test
    fun `init should start with NoCurrencies state`() =
        runTest {
            coEvery { currencyInteractor.getCurrencies() } returns flowOf(emptyList())

            val savedStateHandle = SavedStateHandle()
            val viewModel =
                CurrencyViewModel(
                    testDispatcher,
                    savedStateHandle,
                    currencyInteractor,
                    analyticsSender,
                    currencyUpdateObserver,
                )

            val initialState = viewModel.uiState.value
            assertTrue(initialState is CurrencyUiState.NoCurrencies)
        }

    @Test
    fun `init with empty currency list should update to HasCurrencies with empty list`() =
        runTest {
            coEvery { currencyInteractor.getCurrencies() } returns flowOf(emptyList())

            val savedStateHandle = SavedStateHandle()
            val viewModel =
                CurrencyViewModel(
                    testDispatcher,
                    savedStateHandle,
                    currencyInteractor,
                    analyticsSender,
                    currencyUpdateObserver,
                )
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is CurrencyUiState.HasCurrencies)
            if (state is CurrencyUiState.HasCurrencies) {
                assertTrue(state.currencyList.isEmpty())
                assertEquals(false, state.isLoading)
            }
        }
}
