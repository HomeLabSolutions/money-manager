package com.d9tilov.moneymanager.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val STARTUP_ITERATIONS = 15

@RunWith(AndroidJUnit4::class)
class StartupBenchmarks {
    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun signedInWithoutProfile() {
        measure(CompilationMode.None())
    }

    @Test
    fun signedInWithProfile() {
        measure(CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require))
    }

    private fun measure(compilationMode: CompilationMode) {
        prepareSignedInScenario()
        rule.measureRepeated(
            packageName = TARGET_PACKAGE,
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = STARTUP_ITERATIONS,
            setupBlock = {
                device.executeShellCommand("cmd jobscheduler cancel $TARGET_PACKAGE")
                pressHome()
            },
        ) {
            startAndWaitForExpenseScreen()
        }
    }
}
