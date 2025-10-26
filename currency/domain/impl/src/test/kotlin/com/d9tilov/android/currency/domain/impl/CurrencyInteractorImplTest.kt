package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class CurrencyInteractorImplTest {
    private val currencyRepo: CurrencyRepo = mockk(relaxed = true)
    private val interactor: CurrencyInteractor by lazy { CurrencyInteractorImpl(currencyRepo) }

    private val usdCurrency =
        Currency(
            code = "USD",
            symbol = "$",
            value = BigDecimal.ONE,
            lastUpdateTime = 0L,
        )

    private val eurCurrency =
        Currency(
            code = "EUR",
            symbol = "€",
            value = BigDecimal("0.85"),
            lastUpdateTime = 0L,
        )

    private val mainCurrencyMetaData =
        CurrencyMetaData(
            clientId = "main-currency-client-id",
            code = "USD",
            symbol = "$",
        )

    @Before
    fun setup() {
        every { currencyRepo.getMainCurrency() } returns flowOf(mainCurrencyMetaData)
    }

    @Test
    fun `getCurrencies should return currencies with main currency marked`() =
        runTest {
            val currencies = listOf(usdCurrency, eurCurrency)
            coEvery { currencyRepo.getCurrencies() } returns flowOf(currencies)

            val result = interactor.getCurrencies().first()

            assertEquals(2, result.size)
            assertTrue(result.any { it.code == "USD" })
        }

    @Test
    fun `getMainCurrencyFlow should return main currency from repo`() =
        runTest {
            val currency = interactor.getMainCurrencyFlow().first()

            assertEquals("USD", currency.code)
            assertEquals("$", currency.symbol)
        }

    @Test
    fun `getMainCurrency should return main currency metadata`() =
        runTest {
            val result = interactor.getMainCurrency()

            assertEquals("USD", result.code)
            assertEquals("$", result.symbol)
        }

    @Test
    fun `getCurrencyByCode should return currency from repo`() =
        runTest {
            coEvery { currencyRepo.getCurrencyByCode("EUR") } returns eurCurrency

            val result = interactor.getCurrencyByCode("EUR")

            assertEquals("EUR", result.code)
            assertEquals(BigDecimal("0.85"), result.value)
        }

    @Test
    fun `toTargetCurrency should convert between currencies correctly`() =
        runTest {
            coEvery { currencyRepo.getCurrencyByCode("USD") } returns usdCurrency
            coEvery { currencyRepo.getCurrencyByCode("EUR") } returns eurCurrency

            val result = interactor.toTargetCurrency(BigDecimal(100), "USD", "EUR")

            assertEquals(0, result.compareTo(BigDecimal("85.00")))
        }

    @Test
    fun `toTargetCurrency should return same amount for same currency`() =
        runTest {
            val result = interactor.toTargetCurrency(BigDecimal(100), "USD", "USD")

            assertEquals(BigDecimal(100), result)
            coVerify(exactly = 0) { currencyRepo.getCurrencyByCode(any()) }
        }

    @Test
    fun `toUsd should convert currency to USD correctly`() =
        runTest {
            coEvery { currencyRepo.getCurrencyByCode("EUR") } returns eurCurrency

            val result = interactor.toUsd(BigDecimal(85), "EUR")

            assertEquals(0, result.compareTo(BigDecimal("100.00")))
        }

    @Test
    fun `toUsd should return same amount if currency is already USD`() =
        runTest {
            val result = interactor.toUsd(BigDecimal(100), "USD")

            assertEquals(BigDecimal(100), result)
            coVerify(exactly = 0) { currencyRepo.getCurrencyByCode(any()) }
        }

    @Test
    fun `updateCurrencyRates should return success when update succeeds`() =
        runTest {
            coEvery { currencyRepo.updateCurrencies() } returns Unit

            val result = interactor.updateCurrencyRates()

            assertTrue(result.isSuccess)
            assertEquals(true, result.getOrNull())
        }

    @Test
    fun `updateCurrencyRates should return failure when update fails`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery { currencyRepo.updateCurrencies() } throws exception

            val result = interactor.updateCurrencyRates()

            assertTrue(result.isFailure)
            assertFalse(result.isSuccess)
        }

    @Test
    fun `updateMainCurrency should delegate to repo`() =
        runTest {
            coEvery { currencyRepo.updateMainCurrency("EUR") } returns Unit

            interactor.updateMainCurrency("EUR")

            coVerify(exactly = 1) { currencyRepo.updateMainCurrency("EUR") }
        }
}
