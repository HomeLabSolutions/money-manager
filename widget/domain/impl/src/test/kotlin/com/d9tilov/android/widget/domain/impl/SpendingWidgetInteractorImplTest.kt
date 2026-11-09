package com.d9tilov.android.widget.domain.impl

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.SpendingSummary
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.model.UserProfile
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class SpendingWidgetInteractorImplTest {
    private val transactions = mockk<TransactionInteractor>()
    private val currency = mockk<CurrencyInteractor>()
    private val users = mockk<UserInteractor>()
    private val observeSpendingSummary = mockk<ObserveSpendingSummaryUseCase>()
    private val billingInteractor = mockk<BillingInteractor>()
    private val source =
        SpendingWidgetInteractorImpl(Dispatchers.Unconfined, observeSpendingSummary, users, billingInteractor)
    private val user = UserProfile.EMPTY.copy(uid = "user", showPrepopulate = false)

    @Test
    fun `signed out widget does not read financial data`() =
        runTest {
            every { users.getCurrentUser() } returns flowOf(null)
            assertEquals(SpendingWidgetState.SignedOut, source.observe().first())
            verify { listOf(transactions, currency, observeSpendingSummary, billingInteractor) wasNot Called }
        }

    @Test
    fun `unfinished setup does not display totals`() =
        runTest {
            every { users.getCurrentUser() } returns flowOf(user.copy(showPrepopulate = true))
            assertEquals(SpendingWidgetState.SetupRequired, source.observe().first())
            verify { listOf(transactions, currency, billingInteractor) wasNot Called }
        }

    @Test
    fun `widget respects the same premium access as the home screen`() =
        runTest {
            every { users.getCurrentUser() } returns flowOf(user)
            every { billingInteractor.isPremium() } returns flowOf(false)

            assertEquals(SpendingWidgetState.PremiumRequired, source.observe().first())
            verify { listOf(transactions, currency) wasNot Called }
        }

    @Test
    fun `widget uses the home screen calculations and main currency`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal("125.50")))

            assertEquals(
                SpendingWidgetState.Ready(
                    SpendingSummary(
                        TransactionSpendingTodayModel.NORMAL(BigDecimal("125.50")),
                        BigDecimal("24.75"),
                        BigDecimal("975.25"),
                        CurrencyMetaData.EMPTY.copy(code = "RUB"),
                    ),
                ),
                source.observe().first(),
            )
        }

    @Test
    fun `overspending retains its negative amount`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.OVERSPENDING(BigDecimal("-12.25")))

            val state = source.observe().first() as SpendingWidgetState.Ready

            assertEquals(
                BigDecimal("-12.25"),
                (state.spendingSummary.availableToday as TransactionSpendingTodayModel.OVERSPENDING).trSum,
            )
        }

    @Test
    fun `each refresh recreates date bounded flows and reads the current currency`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal.TEN))
            source.observe().first()
            every { currency.getMainCurrencyFlow() } returns flowOf(CurrencyMetaData.EMPTY.copy(code = "USD"))
            every { transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE) } returns
                flowOf(BigDecimal.ZERO)

            val state = source.observe().first() as SpendingWidgetState.Ready

            assertEquals("USD", state.spendingSummary.currency.code)
            assertEquals(BigDecimal.ZERO, state.spendingSummary.spentToday)
            verify(exactly = 2) {
                transactions.ableToSpendToday()
                transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE)
                transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE)
            }
            verify(exactly = 2) { currency.getMainCurrencyFlow() }
        }

    @Test
    fun `all three amounts update on the existing subscription`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal("125.50")))
            val available =
                MutableStateFlow<TransactionSpendingTodayModel>(
                    TransactionSpendingTodayModel.NORMAL(BigDecimal("125.50")),
                )
            val today = MutableStateFlow(BigDecimal("24.75"))
            val period = MutableStateFlow(BigDecimal("975.25"))
            every { transactions.ableToSpendToday() } returns available
            every { transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE) } returns today
            every { transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE) } returns period
            val states = mutableListOf<SpendingWidgetState>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                source.observe().toList(states)
            }

            available.value = TransactionSpendingTodayModel.NORMAL(BigDecimal("115.50"))
            today.value = BigDecimal("34.75")
            period.value = BigDecimal("985.25")
            runCurrent()

            assertEquals(
                SpendingWidgetState.Ready(
                    SpendingSummary(
                        TransactionSpendingTodayModel.NORMAL(BigDecimal("115.50")),
                        BigDecimal("34.75"),
                        BigDecimal("985.25"),
                        CurrencyMetaData.EMPTY.copy(code = "RUB"),
                    ),
                ),
                states.last(),
            )
            period.value = BigDecimal("1234.56")
            runCurrent()
            assertEquals(
                BigDecimal("1234.56"),
                (states.last() as SpendingWidgetState.Ready).spendingSummary.spentInPeriod,
            )
            verify(exactly = 1) { transactions.ableToSpendToday() }
        }

    @Test
    fun `premium changes hide and restore live amounts`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal.TEN))
            val premium = MutableStateFlow(true)
            every { billingInteractor.isPremium() } returns premium
            val states = mutableListOf<SpendingWidgetState>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                source.observe().toList(states)
            }

            premium.value = false
            runCurrent()
            assertEquals(SpendingWidgetState.PremiumRequired, states.last())

            premium.value = true
            runCurrent()
            assertEquals(
                BigDecimal.TEN,
                (
                    (states.last() as SpendingWidgetState.Ready).spendingSummary.availableToday as
                        TransactionSpendingTodayModel.NORMAL
                ).trSum,
            )
        }

    @Test
    fun `currency and fiscal day changes update the summary`() =
        runTest {
            prepareAmounts(TransactionSpendingTodayModel.NORMAL(BigDecimal.TEN))
            val currencies = MutableStateFlow(CurrencyMetaData.EMPTY.copy(code = "RUB"))
            val profiles = MutableStateFlow<UserProfile?>(user)
            every { currency.getMainCurrencyFlow() } returns currencies
            every { users.getCurrentUser() } returns profiles
            val states = mutableListOf<SpendingWidgetState>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                source.observe().toList(states)
            }

            currencies.value = CurrencyMetaData.EMPTY.copy(code = "USD")
            runCurrent()
            assertEquals("USD", (states.last() as SpendingWidgetState.Ready).spendingSummary.currency.code)

            profiles.value = user.copy(fiscalDay = 15)
            runCurrent()
            verify(exactly = 2) { transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE) }

            profiles.value = null
            runCurrent()
            assertEquals(SpendingWidgetState.SignedOut, states.last())
        }

    private fun prepareAmounts(available: TransactionSpendingTodayModel) {
        every { users.getCurrentUser() } returns flowOf(user)
        every { billingInteractor.isPremium() } returns flowOf(true)
        every { currency.getMainCurrencyFlow() } returns flowOf(CurrencyMetaData.EMPTY.copy(code = "RUB"))
        every { transactions.ableToSpendToday() } returns flowOf(available)
        every { transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE) } returns
            flowOf(BigDecimal("24.75"))
        every { transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE) } returns
            flowOf(BigDecimal("975.25"))
        every { observeSpendingSummary.invoke() } answers {
            combine(
                transactions.ableToSpendToday(),
                transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
                transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE),
                currency.getMainCurrencyFlow(),
            ) { availableToday, spentToday, spentInPeriod, mainCurrency ->
                SpendingSummary(
                    availableToday,
                    spentToday,
                    spentInPeriod,
                    mainCurrency,
                )
            }
        }
    }
}
