package com.d9tilov.moneymanager.widget

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.model.UserProfile
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class SpendingWidgetDataSourceTest {
    private val transactions = mockk<TransactionInteractor>()
    private val currency = mockk<CurrencyInteractor>()
    private val users = mockk<UserInteractor>()
    private val billing = mockk<BillingInteractor>()
    private val source = SpendingWidgetDataSource(transactions, currency, users, billing)
    private val user = UserProfile.EMPTY.copy(uid = "user", showPrepopulate = false)

    @Test
    fun `signed out widget does not read financial data`() =
        runTest {
            assertEquals(SpendingWidgetState.SignedOut, source.load(null))
            verify { listOf(transactions, currency, users, billing) wasNot Called }
        }

    @Test
    fun `missing user or unfinished setup does not display totals`() =
        runTest {
            for (profile in listOf(null, user.copy(showPrepopulate = true), user.copy(uid = "other"))) {
                every { users.getCurrentUser() } returns flowOf(profile)
                assertEquals(SpendingWidgetState.SetupRequired, source.load(user.uid))
            }
            verify { listOf(transactions, currency, billing) wasNot Called }
        }

    @Test
    fun `widget respects the same premium access as the home screen`() =
        runTest {
            every { users.getCurrentUser() } returns flowOf(user)
            every { billing.isPremium() } returns flowOf(false)

            assertEquals(SpendingWidgetState.PremiumRequired, source.load(user.uid))
            verify { listOf(transactions, currency) wasNot Called }
        }

    @Test
    fun `widget uses the home screen calculations and main currency`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal("125.50")))

            assertEquals(
                SpendingWidgetState.Ready("RUB", BigDecimal("125.50"), BigDecimal("24.75"), BigDecimal("975.25")),
                source.load(user.uid),
            )
        }

    @Test
    fun `overspending retains its negative amount`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.OVERSPENDING(BigDecimal("-12.25")))

            val state = source.load(user.uid) as SpendingWidgetState.Ready

            assertEquals(BigDecimal("-12.25"), state.availableToday)
        }

    @Test
    fun `each refresh recreates date bounded flows and reads the current currency`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal.TEN))
            source.load(user.uid)
            coEvery { currency.getMainCurrency() } returns CurrencyMetaData.EMPTY.copy(code = "USD")
            every { transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE) } returns
                flowOf(BigDecimal.ZERO)

            val state = source.load(user.uid) as SpendingWidgetState.Ready

            assertEquals("USD", state.currencyCode)
            assertEquals(BigDecimal.ZERO, state.spentToday)
            verify(exactly = 2) {
                transactions.ableToSpendToday()
                transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE)
                transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE)
            }
            coVerify(exactly = 2) { currency.getMainCurrency() }
        }

    private fun prepareAmounts(available: TransactionSpendingTodayModel) {
        every { users.getCurrentUser() } returns flowOf(user)
        every { billing.isPremium() } returns flowOf(true)
        coEvery { currency.getMainCurrency() } returns CurrencyMetaData.EMPTY.copy(code = "RUB")
        every { transactions.ableToSpendToday() } returns flowOf(available)
        every { transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE) } returns
            flowOf(BigDecimal("24.75"))
        every { transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE) } returns
            flowOf(BigDecimal("975.25"))
    }
}
