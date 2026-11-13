# Application Baseline Profile

`app/src/main/generated/baselineProfiles/baseline-prof.txt` contains ART compilation
rules collected from the application. The adjacent `startup-prof.txt` contains the startup
rules used by R8 to improve DEX layout. AGP consumes both files during a normal
release build using the configured AGP profile source directory, without a device
or the profile generator plugin.

The profile covers signed-in startup through the usable expense screen. The
fixture has a user, USD as its main currency, one expense category and one
income category, and no transactions. Authentication uses the local Firebase
Auth Emulator; no real Firebase account or production credentials are used.
Transaction entry and scrolling are not covered yet. Only the authorized
startup journey should set
`includeInStartupProfile = true`.

## Regenerate

Use a disposable emulator or test device running Android 13 / API 33 or newer.
Older devices require root. The generator uses a synthetic Firebase configuration and a separately
installed package, `com.d9tilov.moneymanager.baselineprofile`.

The target's fixture activity and Auth Emulator provider are compiled only into
the opt-in profile/benchmark variants. They are absent from production releases.
Fixture preparation runs before profile collection and timing. It resets only
the isolated target's preferences and application database.

Start the Auth Emulator on the host and expose it through ADB. Networking can
stay disabled on the Android emulator: Auth uses the reverse-forwarded loopback
port. Do not sign into the isolated package manually.

```shell
adb reverse tcp:9099 tcp:9099
npx --yes firebase-tools@15.34.0 emulators:exec \
  --only auth --project demo-baseline-profile --config baselineprofile/firebase.json \
  './gradlew -PbaselineProfile=true :app:generateBaselineProfile'
```

On an emulator, append:

```shell
-Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR
```

The producer plugin uses `nonMinifiedRelease` so collected rules contain the
original class names. It writes both profiles into `app/src/main/generated/baselineProfiles`.
Commit the generated files with the associated application changes. Regenerate
after significant changes to startup, navigation, dependencies, or DI, including
the pending Metro migration.

The `baselineProfile` property enables the producer module and the plugin's
temporary application variants. Without this property, normal builds do not
configure those variants or run device tests. Profile generation is deliberately
not triggered automatically by assembling a release.

## Measure

Run on a physical device for meaningful performance conclusions. Emulators are
useful for checking generation and installation, but their rendering and host
load make absolute startup times unsuitable as a phone performance estimate.

```shell
adb reverse tcp:9099 tcp:9099
npx --yes firebase-tools@15.34.0 emulators:exec \
  --only auth --project demo-baseline-profile --config baselineprofile/firebase.json \
  './gradlew -PbaselineProfile=true :baselineprofile:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.d9tilov.moneymanager.baselineprofile.StartupBenchmarks'
```

The signed-in startup runs without and with profile compilation. The first mode uses
`CompilationMode.None`; the latter uses
`CompilationMode.Partial(BaselineProfileMode.Require)` with no warmup iterations.
Each mode measures 15 cold starts of the same R8-optimized APK. Profile mode
fails if the packaged profile cannot be installed. Both APK runs have the same DEX layout:
this comparison isolates compilation, not the additional Startup Profile layout
benefit. The packaged profile also contains upstream library rules, so it does
not isolate application rules from library rules.

`StartupTimingMetric` records both TTID and TTFD. The expense screen waits for
the initial currency and expense-category reads, including a valid empty
category list.
Premium-only summaries and background synchronization do not gate these usable
controls. The UI readiness marker and `ReportDrawnWhen` use this same condition.
Fixture setup and authentication requests are outside both metrics. No profile
journey targets the signed-out screen.

The independent startup regression gate uses `CompilationMode.Full` and compares
application revisions. Full compilation cannot measure the ART compilation
benefit of a Baseline Profile. Keep those regression results separate from this
None/Partial comparison.

Results are in `baselineprofile/build/outputs/connected_android_test_additional_output/`.
The Baseline Profile CI workflow runs the same comparison for profile changes
and supports manually regenerating profiles. Generated files are uploaded for
review; CI does not commit changes or use production credentials.

## Verify a release

Build with the usual release Firebase/signing configuration:

```shell
./gradlew :app:assembleRelease :app:bundleRelease
unzip -l app/build/outputs/apk/release/app-release.apk 'assets/dexopt/*'
unzip -l app/build/outputs/bundle/release/app-release.aab '*baseline.prof*'
```

The APK must contain `assets/dexopt/baseline.prof` and `baseline.profm`; the AAB
contains profiles under `BUNDLE-METADATA/com.android.tools.build.profiles/`.
The compiled profile must stay below 1.5 MB. R8 rewrites the collected rules for
the optimized release, while the text source profiles stay unobfuscated.

References: [generation](https://developer.android.com/topic/performance/baselineprofiles/create-baselineprofile),
[configuration](https://developer.android.com/topic/performance/baselineprofiles/configure-baselineprofiles),
[measurement](https://developer.android.com/topic/performance/baselineprofiles/measure-baselineprofile),
[Startup Profiles](https://developer.android.com/topic/performance/baselineprofiles/dex-layout-optimizations).
