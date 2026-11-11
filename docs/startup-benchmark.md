# Startup performance gate

The `benchmark` module measures cold process startup to initial display (TTID)
with AndroidX Macrobenchmark 1.5.0. The target is a non-debuggable, profileable,
R8-optimized copy of release, signed with the debug key and installed as
`com.d9tilov.moneymanager.benchmark`. It does not replace the normal application.
The module and target variant are enabled only with `-PstartupBenchmark=true`.

## Scenario and comparison

Both APKs use the same test APK and harness, `CompilationMode.Full`, a fresh
signed-out fixture per batch, and disabled Wi-Fi/mobile data. Each batch has
15 cold starts; three alternating batches per version yield 45 samples each.
The gate compares the median of all candidate samples with the median of all
reference samples. An increase greater than 5% fails; exactly 5% passes.
Missing reports, failed instrumentation, unequal sample counts, invalid samples,
or too few samples fail the job instead of bypassing the gate.

WorkManager jobs for the disposable package are cancelled between iterations.
Macrobenchmark's `killProcessDelayMillis=1000` gives process shutdown time to settle
before its cold-start assertion and cache clearing. Both run outside the measured
startup; `StartupMode.COLD` and the assertion that no process is running remain
enabled. Failed instrumentation also saves filtered benchmark/device error logs.

The previous APK is remeasured on the same runner/emulator as the candidate.
This avoids treating hardware differences between GitHub-hosted runners as an
application regression. The previously recorded measurement is retained in the
report when a reference artifact exists, but the gate uses the paired measurement.
Hosted-emulator measurements are a relative regression signal, not latency
representative of physical end-user devices. This scenario does not measure
authenticated home-screen loading, first-ever installation, or TTFD.

The harness creates a synthetic Firebase configuration for the benchmark-only
package. No production credentials or account are required. Networking stays off
during measurement, and the disposable package is uninstalled before restoring
the device's network state. Existing user application data is left intact.

## CI and merge protection

`Android CI` calls `startup-benchmark.yml` for pull requests and develop pushes.
The existing required `build` check now aggregates Android checks and the startup
gate. Its final step explicitly fails if either dependency failed, was cancelled,
or was skipped. Existing branch protection requiring `build` therefore blocks
merging startup regressions without adding a new required-check name.
Merge-queue events run the same gate. Manual runs use the distinct `Manual checks`
name, so a manual comparison against another revision cannot satisfy the PR's
required `build` check.

Pull requests compare their merge revision with the PR's base SHA. Develop pushes
compare the pushed revision with the previous develop SHA. After a successful
develop comparison, `startup-baseline` stores the candidate APK, its SHA-256,
source commit, median and device metadata for 30 days. Future runs reuse it only
from a successful develop push with the exact required source SHA and matching
APK digest. If the artifact is missing or expired, CI rebuilds that reference;
it still runs the comparison.

CI uses the standard Ubuntu 24.04 GitHub-hosted runner and an API 35 x86_64
Google APIs emulator with KVM. Standard hosted runner compute is free for public
repositories. The emulator action and artifact/Gradle actions are pinned to
commits. The benchmark job receives read-only repository/Actions permissions.
Results, JSON samples and build logs are artifacts; traces are retained on failure.

The release-version automation waits up to 55 minutes for CI so its deadline
allows for the additional startup measurement job.

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

To compare Hilt and Metro in CI, manually run `Startup benchmark` with
`baseline_ref=develop` and `candidate_ref=migration/metro`. Manual comparisons do
not publish a develop reference. A regression still fails the manual workflow,
and its samples and traces remain available for investigation.

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
