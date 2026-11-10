package com.d9tilov.android.widget.domain.impl

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class SpendingWidgetKoinTest {
    @Test
    fun `widget constructor resolves its qualified dispatcher through the production module`() =
        runTest {
            val users =
                mockk<UserInteractor> {
                    every { getCurrentUser() } returns flowOf(null)
                }
            val dispatcher = StandardTestDispatcher(testScheduler)
            val application =
                koinApplication {
                    modules(
                        spendingWidgetInteractorImplModule,
                        module {
                            single<CoroutineDispatcher>(named(DISPATCHER_IO)) { dispatcher }
                            single<ObserveSpendingSummaryUseCase> { mockk() }
                            single<UserInteractor> { users }
                            single<BillingInteractor> { mockk() }
                        },
                    )
                }
            try {
                val interactor = application.koin.get<SpendingWidgetInteractor>()
                assertEquals(SpendingWidgetState.SignedOut, interactor.observe().first())
            } finally {
                application.close()
            }
        }
}
