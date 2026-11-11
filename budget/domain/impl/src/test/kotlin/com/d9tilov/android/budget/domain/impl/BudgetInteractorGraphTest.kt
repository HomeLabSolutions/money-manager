package com.d9tilov.android.budget.domain.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import io.mockk.mockk
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Test

class BudgetInteractorGraphTest {
    @Test
    fun `budget interactor is shared within one application graph only`() {
        val factory = createGraphFactory<BudgetInteractorTestGraph.Factory>()
        val graph = factory.create(mockk<BudgetRepo>(), mockk<CurrencyInteractor>())
        val anotherGraph = factory.create(mockk<BudgetRepo>(), mockk<CurrencyInteractor>())

        assertSame(graph.budgetInteractor, graph.budgetInteractor)
        assertNotSame(graph.budgetInteractor, anotherGraph.budgetInteractor)
    }
}

@DependencyGraph(AppScope::class)
internal interface BudgetInteractorTestGraph {
    val budgetInteractor: BudgetInteractor

    @Binds
    val BudgetInteractorImpl.bindBudgetInteractor: BudgetInteractor

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides budgetRepo: BudgetRepo,
            @Provides currencyInteractor: CurrencyInteractor,
        ): BudgetInteractorTestGraph
    }
}
