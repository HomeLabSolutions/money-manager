#!/usr/bin/env bash
set -euo pipefail

# This workflow owns a disposable emulator and never installs the production package.
adb shell svc wifi disable
adb shell svc data disable
adb reverse tcp:9099 tcp:9099
adb shell cmd location set-location-enabled false

if [[ "${GENERATE_PROFILE:-false}" == "true" ]]; then
  ./gradlew -PbaselineProfile=true :app:generateBaselineProfile \
    -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR
fi

./gradlew -PbaselineProfile=true :baselineprofile:connectedBenchmarkReleaseAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.d9tilov.moneymanager.baselineprofile.StartupBenchmarks \
  -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR
