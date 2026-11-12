package com.d9tilov.android.common.android.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class MetroWorkerFactoryTest {
    @Test
    fun `worker factory defers child creation and forwards runtime arguments`() {
        val context = mockk<Context>()
        val parameters = mockk<WorkerParameters>()
        val worker = mockk<ListenableWorker>()
        val childFactory = mockk<ChildWorkerFactory>()
        var createdFactories = 0
        every { childFactory.create(context, parameters) } returns worker
        val factory =
            MetroWorkerFactory(
                mapOf(
                    CoroutineWorker::class to {
                        createdFactories++
                        childFactory
                    },
                ),
            )

        assertEquals(0, createdFactories)
        assertSame(worker, factory.createWorker(context, CoroutineWorker::class.java.name, parameters))
        assertEquals(1, createdFactories)
        verify(exactly = 1) { childFactory.create(context, parameters) }
    }

    @Test
    fun `unknown worker does not create child factories`() {
        var createdFactories = 0
        val factory =
            MetroWorkerFactory(
                mapOf(
                    CoroutineWorker::class to {
                        createdFactories++
                        mockk<ChildWorkerFactory>()
                    },
                ),
            )

        assertNull(factory.createWorker(mockk<Context>(), "unknown.Worker", mockk<WorkerParameters>()))
        assertEquals(0, createdFactories)
    }
}
