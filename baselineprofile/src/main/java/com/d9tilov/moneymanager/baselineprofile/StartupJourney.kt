package com.d9tilov.moneymanager.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

internal const val TARGET_PACKAGE = "com.d9tilov.moneymanager.baselineprofile"
private const val SCREEN_TIMEOUT_MILLIS = 30_000L

internal fun prepareSignedInScenario() {
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    device.executeShellCommand("am force-stop $TARGET_PACKAGE")
    device.executeShellCommand("pm grant $TARGET_PACKAGE android.permission.ACCESS_COARSE_LOCATION")
    device.executeShellCommand("pm grant $TARGET_PACKAGE android.permission.ACCESS_FINE_LOCATION")
    device.executeShellCommand(
        "am start -W -n $TARGET_PACKAGE/com.d9tilov.moneymanager.baselinefixture.FixtureActivity",
    )
    check(device.wait(Until.hasObject(By.desc("baseline-fixture-ready")), SCREEN_TIMEOUT_MILLIS)) {
        "Could not prepare startup fixture. Check the local Auth Emulator and fixture activity logs."
    }
}

internal fun MacrobenchmarkScope.startAndWaitForExpenseScreen() {
    startActivityAndWait()
    check(device.wait(Until.hasObject(By.res("IncomeExpenseReady")), SCREEN_TIMEOUT_MILLIS)) {
        "Expected authorized expense screen after startup"
    }
    device.waitForIdle()
}
