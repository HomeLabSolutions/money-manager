# Startup performance gate

The `benchmark` module measures cold process startup to initial display (TTID)
with AndroidX Macrobenchmark 1.5.0. The target is a non-debuggable, profileable,
R8-optimized copy of release, signed with the debug key and installed as
`com.d9tilov.moneymanager.benchmark`. It does not replace the normal application.
The module and target variant are enabled only with `-PstartupBenchmark=true`.

## Scenario and comparison

The candidate uses the Macrobenchmark test harness, `CompilationMode.Full`, a
fresh signed-out fixture per batch, and disabled Wi-Fi/mobile data. Each batch
has 15 cold starts; three batches yield 45 samples. The gate compares its median
with the saved median from the last published version. An increase greater than
5% fails; exactly 5% passes.
Missing reports, failed instrumentation, invalid samples, or too few samples
fail the job instead of bypassing the gate.

WorkManager jobs for the disposable package are cancelled between iterations.
Macrobenchmark's `killProcessDelayMillis=1000` gives process shutdown time to settle
before its cold-start assertion and cache clearing. Both run outside the measured
startup; `StartupMode.COLD` and the assertion that no process is running remain
enabled. Failed instrumentation also saves filtered benchmark/device error logs.

Only the candidate is measured. The previous APK is not rebuilt or run again;
its median is stored in the `startup-benchmark-baseline` branch. Hosted-emulator
measurements are a relative regression signal, not latency representative of
physical end-user devices. This scenario does not measure authenticated
home-screen loading, first-ever installation, or TTFD.

The harness creates a synthetic Firebase configuration for the benchmark-only
package. No production credentials or account are required. Networking stays off
during measurement, and the disposable package is uninstalled before restoring
the device's network state. Existing user application data is left intact.

## Release gate

The benchmark runs only in the Google Play release workflow, immediately before
uploading the AAB. A failed benchmark blocks the upload. After Play accepts the
release, the workflow updates the `startup-benchmark-baseline` branch with the
candidate median and source SHA. Failed measurements and uploads do not move the
baseline. Android CI and pull requests do not run this benchmark.

The first release needs the current published version's median. Supply it once
as `initial_startup_baseline_ms` when starting the Play workflow; after a
successful publish the workflow creates the baseline branch, and later releases
read and update it automatically.

CI uses the standard Ubuntu 24.04 GitHub-hosted runner and an API 29 x86_64
Google APIs emulator with KVM. Standard hosted runner compute is free for public
repositories. The emulator action and artifact/Gradle actions are pinned to
commits. The benchmark job receives read-only repository/Actions permissions.
Results, JSON samples and build logs are artifacts; traces are retained on failure.

## Run locally or compare DI implementations

Start an API 34+ emulator or connect a test device. Build both revisions using
the same harness without switching the working branch:

```sh
python3 .github/scripts/startup_benchmark.py build \
  --baseline-ref origin/develop --candidate-ref origin/migration/metro \
  --output /private/tmp/money-manager-startup
python3 .github/scripts/startup_benchmark.py measure \
  --device emulator-5554 --output /private/tmp/money-manager-startup
```

The build command extracts tracked files for each resolved commit into disposable
directories, overlays the harness and builds both APKs. It creates no Git worktrees.
The resulting `comparison.md` and `comparison.json` identify both exact source
SHAs, APK digests, raw samples and device environment. Local and CI measurements
must not be compared as absolute values across different devices/architectures.

For an ad hoc measurement, manually run `Startup benchmark`, provide a candidate
revision and baseline median. Manual runs do not update the release baseline.

For a single source checkout, prepare the synthetic configuration and build:

```sh
python3 .github/scripts/prepare_startup_benchmark.py --target .
./gradlew :app:assembleBenchmark :benchmark:assembleBenchmark \
  -PstartupBenchmark=true -x :app:uploadCrashlyticsMappingFileBenchmark
```

The generated `app/src/benchmark/google-services.json` is ignored by Git.

## References

- [Macrobenchmark setup](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview)
- [StartupTimingMetric](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-metrics)
- [Macrobenchmark instrumentation arguments](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
- [GitHub Actions billing](https://docs.github.com/en/billing/concepts/product-billing/github-actions)
