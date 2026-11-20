package com.d9tilov.moneymanager.baselineprofile

import android.util.Log
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Condition
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import java.util.regex.Pattern

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
    val fixtureResult = By.desc(Pattern.compile("baseline-fixture-(ready|error)"))
    val launcherAnrDialog =
        By.res("android:id/parentPanel").hasDescendant(
            By.res("android:id/alertTitle").text("Pixel Launcher isn't responding"),
        )
    val closeApp = By.res("android:id/aerr_close")
    val result =
        device.wait(
            Condition { currentDevice ->
                currentDevice.findObject(launcherAnrDialog)?.findObject(closeApp)?.let { button ->
                    button.click()
                    Log.w("StartupJourney", "Dismissed Pixel Launcher ANR while preparing the startup fixture")
                }
                currentDevice.findObject(fixtureResult)
            },
            SCREEN_TIMEOUT_MILLIS,
        )
    checkNotNull(result) {
        "Startup fixture timed out. Check the local Auth Emulator and fixture activity logs."
    }
    check(result.contentDescription == "baseline-fixture-ready") {
        "Startup fixture preparation failed: ${result.text}"
    }
}

internal fun MacrobenchmarkScope.startAndWaitForExpenseScreen() {
    startActivityAndWait()
    check(device.wait(Until.hasObject(By.res("IncomeExpenseReady")), SCREEN_TIMEOUT_MILLIS)) {
        "Expected authorized expense screen after startup"
    }
    device.waitForIdle()
}
