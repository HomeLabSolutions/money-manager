#!/usr/bin/env bash
set -euo pipefail

report_failure() {
  local status=$?
  trap - ERR
  set +e
  printf '\nBaseline Profile generation failed; test reports follow.\n'
  while IFS= read -r report; do
    printf '\nReport: %s\n' "$report"
    cat "$report"
  done < <(rg --files baselineprofile/build/outputs/androidTest-results -g '*.xml')
  mkdir -p baselineprofile/build/diagnostics
  adb logcat -d -v threadtime -t 1000 > baselineprofile/build/diagnostics/logcat.txt
  cat baselineprofile/build/diagnostics/logcat.txt
  exit "$status"
}
trap report_failure ERR

# CI owns this disposable emulator and profiles the isolated .baselineprofile package.
adb shell svc wifi disable
adb shell svc data disable
adb reverse tcp:9099 tcp:9099
adb shell cmd location set-location-enabled false

# A successful release must contain profiles produced by this run.
profile_directory=app/src/main/generated/baselineProfiles
rm -f -- "$profile_directory/baseline-prof.txt" "$profile_directory/startup-prof.txt"

./gradlew -PbaselineProfile=true :app:generateBaselineProfile \
  -Pandroid.testInstrumentationRunnerArguments.class=com.d9tilov.moneymanager.baselineprofile.BaselineProfileGenerator \
  -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR

test -s "$profile_directory/baseline-prof.txt"
test -s "$profile_directory/startup-prof.txt"
