package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.user.domain.contract.UserInteractor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TransactionInteractorCurrencyLookupTest {
    private val transactionRepo: TransactionRepo = mockk(relaxed = true)
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)
    private val userInteractor: UserInteractor = mockk(relaxed = true)
    private val interactor =
        TransactionInteractorImpl(
            transactionRepo = transactionRepo,
            regularTransactionInteractor = mockk<RegularTransactionInteractor>(relaxed = true),
            categoryInteractor = mockk<CategoryInteractor>(relaxed = true),
            userInteractor = userInteractor,
            currencyInteractor = currencyInteractor,
            budgetInteractor = mockk<BudgetInteractor>(relaxed = true),
        )

    @Test
    fun `getSumInPeriod should load target currency once per emission`() =
        runTest {
            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)
            val transactions = (1L..3L).map(::transaction)
            val euro =
                Currency(
                    code = "EUR",
                    symbol = "€",
                    value = BigDecimal("0.5"),
                    lastUpdateTime = 0L,
                )

            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(
                    from,
                    to,
                    TransactionType.EXPENSE,
                    onlyInStatistics = true,
                    withRegular = true,
                )
            } returns flowOf(transactions)
            coEvery { currencyInteractor.getCurrencyByCode("EUR") } returns euro

            val result =
                interactor
                    .getSumInPeriod(
                        from = from,
                        to = to,
                        transactionType = TransactionType.EXPENSE,
                        currencyCode = "EUR",
                        inStatistics = true,
                    ).first()

            assertEquals(0, result.compareTo(BigDecimal("15")))
            coVerify(exactly = 1) { currencyInteractor.getCurrencyByCode("EUR") }
        }

    @Test
    fun `getSumInFiscalPeriod should load each current rate once per emission`() =
        runTest {
            val transactions = (1L..3L).map(::transaction)
            coEvery { userInteractor.getFiscalDay() } returns 1
            coEvery { currencyInteractor.getMainCurrency() } returns
                CurrencyMetaData(clientId = "client", code = "EUR", symbol = "€")
            coEvery { currencyInteractor.getCurrencyByCode("EUR") } returns currency("EUR", "0.5")
            coEvery { currencyInteractor.getCurrencyByCode("GBP") } returns currency("GBP", "2")
            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(
                    any(),
                    any(),
                    TransactionType.INCOME,
                    onlyInStatistics = true,
                    withRegular = true,
                )
            } returns flowOf(transactions)
            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(
                    any(),
                    any(),
                    TransactionType.EXPENSE,
                    onlyInStatistics = true,
                    withRegular = true,
                )
            } returns flowOf(emptyList())

            val result = interactor.getSumInFiscalPeriod().first()

            assertEquals(0, result.compareTo(BigDecimal("7.5")))
            coVerify(exactly = 1) { currencyInteractor.getCurrencyByCode("EUR") }
            coVerify(exactly = 1) { currencyInteractor.getCurrencyByCode("GBP") }
        }

    private fun currency(
        code: String,
        rate: String,
    ) = Currency(
        code = code,
        symbol = code,
        value = BigDecimal(rate),
        lastUpdateTime = 0L,
    )

    private fun transaction(id: Long) =
        TransactionDataModel(
            id = id,
            clientId = "transaction-$id",
            type = TransactionType.EXPENSE,
            categoryId = 1L,
            currencyCode = "GBP",
            sum = BigDecimal.TEN,
            usdSum = BigDecimal.TEN,
            date = LocalDateTime(2024, 1, 1, 0, 0),
            description = "Test",
            qrCode = "",
            inStatistics = true,
            isRegular = false,
            location = LocationData(0.0, 0.0),
            photoUri = "",
        )
}
