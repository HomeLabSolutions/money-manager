package com.d9tilov.moneymanager.startup

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun coldStartup() {
        val iterations =
            InstrumentationRegistry.getArguments().getString("startupIterations")?.toInt()
                ?: DEFAULT_ITERATIONS
        require(iterations >= MIN_ITERATIONS) { "At least $MIN_ITERATIONS iterations are required" }

        benchmarkRule.measureRepeated(
            packageName = TARGET_PACKAGE,
            metrics = listOf(StartupTimingMetric()),
            compilationMode = CompilationMode.Full(),
            startupMode = StartupMode.COLD,
            iterations = iterations,
            setupBlock = {
                pressHome()
                // Startup schedules WorkManager jobs; cancel them between cold launches.
                device.executeShellCommand("cmd jobscheduler cancel $TARGET_PACKAGE")
            },
        ) {
            startActivityAndWait()
        }
    }

    private companion object {
        const val TARGET_PACKAGE = "com.d9tilov.moneymanager.benchmark"
        const val DEFAULT_ITERATIONS = 15
        const val MIN_ITERATIONS = 10
    }
}
