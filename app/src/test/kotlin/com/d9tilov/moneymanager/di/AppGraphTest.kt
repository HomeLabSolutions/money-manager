package com.d9tilov.moneymanager.di

import android.app.Application
import android.content.Context
import androidx.work.WorkerParameters
import com.d9tilov.android.currency.ui.CurrencyViewModel
import com.d9tilov.android.transaction.ui.vm.TransactionInfoViewModel
import com.d9tilov.moneymanager.home.MainViewModel
import dev.zacsweers.metro.createGraphFactory
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AppGraphTest {
    private fun createGraph(): AppGraph =
        createGraphFactory<AppGraph.Factory>().create(mockk<Application>(relaxed = true))

    @Test
    fun `production graph creates factories without eagerly opening feature dependencies`() {
        val graph = createGraph()

        assertSame(graph.metroViewModelFactory, graph.metroViewModelFactory)
        assertSame(graph.workerFactory, graph.workerFactory)
        assertTrue(MainViewModel::class in graph.viewModelProviders)
    }

    @Test
    fun `navigation view models retain assisted factories for saved state`() {
        val graph = createGraph()

        assertNotNull(graph.assistedFactoryProviders.getValue(CurrencyViewModel::class).invoke())
        assertNotNull(graph.assistedFactoryProviders.getValue(TransactionInfoViewModel::class).invoke())
    }

    @Test
    fun `unknown worker allows WorkManager default construction without opening feature dependencies`() {
        val graph = createGraph()

        assertNull(
            graph.workerFactory.createWorker(
                mockk<Context>(),
                "unknown.Worker",
                mockk<WorkerParameters>(),
            ),
        )
    }
}
