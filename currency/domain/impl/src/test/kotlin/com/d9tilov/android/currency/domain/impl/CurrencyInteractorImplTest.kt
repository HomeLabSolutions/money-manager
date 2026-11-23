package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CurrencyInteractorImplTest {
    private val currencyRepo: CurrencyRepo = mockk(relaxed = true)
    private val interactor: CurrencyInteractor by lazy { CurrencyInteractorImpl(currencyRepo) }

    private val usdCurrency =
        CurrencyRate(
            code = "USD",
            symbol = "$",
            value = Decimal.ONE,
            lastUpdateTime = 0L,
        )

    private val eurCurrency =
        CurrencyRate(
            code = "EUR",
            symbol = "€",
            value = Decimal("0.85"),
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
    fun `updateCurrencyRates should propagate cancellation`() =
        runTest {
            val cancellation = CancellationException("Worker stopped")
            coEvery { currencyRepo.updateCurrencies() } throws cancellation

            val result = runCatching { interactor.updateCurrencyRates() }

            assertSame(cancellation, result.exceptionOrNull())
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
            assertEquals(Decimal("0.85"), result.value)
        }

    @Test
    fun `toTargetCurrency should convert between currencies correctly`() =
        runTest {
            coEvery { currencyRepo.getCurrencyByCode("USD") } returns usdCurrency
            coEvery { currencyRepo.getCurrencyByCode("EUR") } returns eurCurrency

            val result = interactor.toTargetCurrency(Decimal(100), "USD", "EUR")

            assertEquals(0, result.compareTo(Decimal("85.00")))
        }

    @Test
    fun `toTargetCurrency rounds the converted amount once at a half-cent tie`() =
        runTest {
            coEvery { currencyRepo.getCurrencyByCode("USD") } returns usdCurrency
            coEvery { currencyRepo.getCurrencyByCode("EUR") } returns eurCurrency.copy(value = Decimal(14))

            assertEquals(Decimal("0.01"), interactor.toTargetCurrency(Decimal("0.07"), "EUR", "USD"))
            assertEquals(Decimal("-0.01"), interactor.toTargetCurrency(Decimal("-0.07"), "EUR", "USD"))
        }

    @Test
    fun `toTargetCurrency should return same amount for same currency`() =
        runTest {
            val result = interactor.toTargetCurrency(Decimal(100), "USD", "USD")

            assertEquals(Decimal(100), result)
            coVerify(exactly = 0) { currencyRepo.getCurrencyByCode(any()) }
        }

    @Test
    fun `toUsd should convert currency to USD correctly`() =
        runTest {
            coEvery { currencyRepo.getCurrencyByCode("EUR") } returns eurCurrency

            val result = interactor.toUsd(Decimal(85), "EUR")

            assertEquals(0, result.compareTo(Decimal("100.00")))
        }

    @Test
    fun `toUsd should return same amount if currency is already USD`() =
        runTest {
            val result = interactor.toUsd(Decimal(100), "USD")

            assertEquals(Decimal(100), result)
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
