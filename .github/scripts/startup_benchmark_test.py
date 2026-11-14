import copy
import json
from pathlib import Path
import tempfile
import unittest
import subprocess
import sys
from unittest.mock import patch

from compare_startup_benchmark import METRIC, TEST_CLASS, compare, read_samples
from prepare_startup_benchmark import PACKAGE, prepare
from startup_benchmark import cached_baseline, sha256


class StartupComparisonTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.directory = Path(self.temp.name)

    def report(self, name, samples):
        path = self.directory / name
        path.write_text(json.dumps({"benchmarks": [{
            "className": TEST_CLASS, "name": "coldStartup", "metrics": {METRIC: {"runs": samples}},
        }]}))
        return path

    def test_exactly_five_percent_is_allowed(self):
        baseline = self.report("baseline.json", [100.0] * 10)
        candidate = self.report("candidate.json", [105.0] * 10)
        self.assertTrue(compare([baseline], [candidate])["passed"])

    def test_more_than_five_percent_blocks(self):
        baseline = self.report("baseline.json", [100.0] * 10)
        candidate = self.report("candidate.json", [105.001] * 10)
        result = compare([baseline], [candidate])
        self.assertFalse(result["passed"])
        self.assertGreater(result["regressionPercent"], 5)

    def test_uses_median_of_samples_rather_than_maximum_or_mean(self):
        baseline = self.report("baseline.json", [100.0] * 10)
        candidate = self.report("candidate.json", [99.0] * 9 + [1000.0])
        self.assertTrue(compare([baseline], [candidate])["passed"])

    def test_aggregates_all_rounds(self):
        baseline = [self.report("b1.json", [100.0] * 10), self.report("b2.json", [200.0] * 10)]
        candidate = [self.report("c1.json", [100.0] * 10), self.report("c2.json", [210.0] * 10)]
        result = compare(baseline, candidate)
        self.assertEqual(result["baseline"]["sampleCount"], 20)
        self.assertEqual(result["baseline"]["medianMs"], 150.0)
        self.assertEqual(result["candidate"]["medianMs"], 155.0)

    def test_invalid_or_incomplete_samples_fail_closed(self):
        for samples in [[100.0] * 9, [0] * 10, [-1] * 10, [True] * 10,
                        [float("nan")] * 10, [float("inf")] * 10, ["100"] * 10]:
            with self.subTest(samples=samples), self.assertRaises(ValueError):
                read_samples(self.report("bad.json", samples))

    def test_missing_or_ambiguous_benchmark_is_rejected(self):
        path = self.report("ambiguous.json", [100.0] * 10)
        data = json.loads(path.read_text())
        data["benchmarks"].append(copy.deepcopy(data["benchmarks"][0]))
        path.write_text(json.dumps(data))
        with self.assertRaises(ValueError):
            read_samples(path)
        data["benchmarks"] = []
        path.write_text(json.dumps(data))
        with self.assertRaises(ValueError):
            read_samples(path)

    def test_different_sample_counts_are_rejected(self):
        baseline = self.report("baseline.json", [100.0] * 10)
        candidate = self.report("candidate.json", [100.0] * 15)
        with self.assertRaises(ValueError):
            compare([baseline], [candidate])

    def test_missing_startup_metric_does_not_fall_back_to_another_metric(self):
        path = self.report("wrong-metric.json", [100.0] * 10)
        data = json.loads(path.read_text())
        data["benchmarks"][0]["metrics"] = {"frameDurationCpuMs": {"runs": [100.0] * 10}}
        path.write_text(json.dumps(data))
        with self.assertRaises(KeyError):
            read_samples(path)

    def test_cli_returns_failure_and_writes_evidence_for_a_regression(self):
        baseline = self.report("baseline.json", [100.0] * 10)
        candidate = self.report("candidate.json", [106.0] * 10)
        output = self.directory / "comparison.json"
        process = subprocess.run([
            sys.executable, str(Path(__file__).with_name("compare_startup_benchmark.py")),
            "--baseline", str(baseline), "--candidate", str(candidate), "--output", str(output),
        ], capture_output=True, text=True, check=False)
        self.assertEqual(process.returncode, 1)
        self.assertFalse(json.loads(output.read_text())["passed"])
        self.assertIn("more than 5%", process.stderr)


class StartupHarnessTest(unittest.TestCase):
    @patch.dict("os.environ", {"GH_TOKEN": "test-token", "GITHUB_REPOSITORY": "owner/repo"})
    @patch("startup_benchmark.command", return_value='[{"databaseId": 123}]')
    @patch("startup_benchmark.subprocess.run")
    def test_cached_reference_requires_exact_source_package_and_digest(self, download, _):
        download.return_value.returncode = 0
        with tempfile.TemporaryDirectory() as temp:
            output = Path(temp)
            cache = output / "cached-123"
            cache.mkdir()
            apk = cache / "app-benchmark.apk"
            apk.write_bytes(b"reference APK")
            source = "a" * 40
            record = {"sourceSha": source, "packageName": PACKAGE, "apkSha256": sha256(apk)}
            metadata = cache / "measurement.json"
            metadata.write_text(json.dumps(record))
            self.assertIsNotNone(cached_baseline(source, output))
            self.assertIsNone(cached_baseline("b" * 40, output))
            record["packageName"] = "another.application"
            metadata.write_text(json.dumps(record))
            self.assertIsNone(cached_baseline(source, output))
            record["packageName"] = PACKAGE
            metadata.write_text(json.dumps(record))
            apk.write_bytes(b"modified APK")
            self.assertIsNone(cached_baseline(source, output))

    def test_overlay_is_idempotent_and_preserves_framework_configuration(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            source, target = root / "source", root / "target"
            (source / "benchmark").mkdir(parents=True)
            (source / "benchmark/build.gradle.kts").write_text("// identical harness\n")
            plugin = source / "buildSrc/src/main/kotlin/com/android/moneymanager/gradle/plugins"
            plugin.mkdir(parents=True)
            (plugin / "AndroidApplicationStartupBenchmarkConventionPlugin.kt").write_text("// plugin\n")
            (source / "buildSrc/build.gradle.kts").write_text("gradlePlugin { plugins { } }\n")
            (target / "app").mkdir(parents=True)
            (target / "buildSrc").mkdir(parents=True)
            target_build_src = target / "buildSrc/build.gradle.kts"
            target_build_src.write_text("gradlePlugin { plugins { } }\n")
            (target / "settings.gradle.kts").write_text('rootProject.name = "MoneyManager"\n')
            app = target / "app/build.gradle.kts"
            app.write_text('plugins { id("moneymanager.di") }\n')
            prepare(source, target)
            first_app = app.read_text()
            first_settings = (target / "settings.gradle.kts").read_text()
            prepare(source, target)
            self.assertEqual(app.read_text(), first_app)
            self.assertEqual((target / "settings.gradle.kts").read_text(), first_settings)
            self.assertIn('id("moneymanager.di")', first_app)
            self.assertIn('moneymanager.android.application.startup-benchmark', first_app)
            plugin_target = target / "buildSrc/src/main/kotlin/com/android/moneymanager/gradle/plugins/AndroidApplicationStartupBenchmarkConventionPlugin.kt"
            self.assertTrue(plugin_target.is_file())
            self.assertIn('moneymanager.android.application.startup-benchmark', target_build_src.read_text())
            self.assertEqual(first_settings.count('include(":benchmark")'), 1)
            config = json.loads((target / "app/src/benchmark/google-services.json").read_text())
            self.assertEqual(config["client"][0]["client_info"]["android_client_info"]["package_name"], PACKAGE)
            self.assertEqual(config["project_info"]["project_id"], "startup-benchmark")


if __name__ == "__main__":
    unittest.main()
