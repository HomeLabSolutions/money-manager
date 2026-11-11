import json
import io
from argparse import Namespace
from contextlib import redirect_stdout
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from baseline_profile_artifact import PROFILE_FILES, apply_profile_artifact, pack_profiles
from compare_startup_benchmark import METRIC, TEST_CLASS
from startup_baseline import PROTOCOL
from startup_benchmark import measure_candidate, prepare_profiles, sha256


class BaselineProfileArtifactTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.profiles = self.root / "generated"
        self.profiles.mkdir()
        for name in PROFILE_FILES:
            (self.profiles / name).write_text("HSPLcom/example/MainActivity;->onCreate()V\n")
        self.source_sha = "a" * 40
        self.artifact = self.root / "artifact"

    def test_gate_receives_the_same_profiles_and_source_as_the_release(self):
        metadata = pack_profiles(self.source_sha, self.profiles, self.artifact)
        checkout, output = self.root / "checkout", self.root / "output"
        output.mkdir()
        copied = prepare_profiles(self.artifact, checkout, output, "candidate", self.source_sha)
        self.assertEqual(copied, metadata)
        for name in PROFILE_FILES:
            expected = (self.profiles / name).read_bytes()
            self.assertEqual((checkout / "app/src/main/generated/baselineProfiles" / name).read_bytes(), expected)
            self.assertEqual((output / "candidate-profile" / name).read_bytes(), expected)

    def test_profiles_for_another_source_revision_are_rejected(self):
        pack_profiles(self.source_sha, self.profiles, self.artifact)
        with self.assertRaisesRegex(ValueError, "source SHA"):
            apply_profile_artifact(self.artifact, self.root / "destination", "b" * 40)
        self.assertFalse((self.root / "destination").exists())

    def test_modified_profile_is_rejected_before_copying(self):
        pack_profiles(self.source_sha, self.profiles, self.artifact)
        (self.artifact / "startup-prof.txt").write_text("modified profile")
        with self.assertRaisesRegex(ValueError, "digest mismatch"):
            apply_profile_artifact(self.artifact, self.root / "destination", self.source_sha)
        self.assertFalse((self.root / "destination").exists())

    def test_missing_or_empty_startup_profile_blocks_the_snapshot(self):
        (self.profiles / "startup-prof.txt").write_text("")
        with self.assertRaisesRegex(ValueError, "missing or empty"):
            pack_profiles(self.source_sha, self.profiles, self.artifact)
        (self.profiles / "startup-prof.txt").unlink()
        with self.assertRaisesRegex(ValueError, "missing or empty"):
            pack_profiles(self.source_sha, self.profiles, self.artifact)

    def test_incomplete_metadata_is_rejected(self):
        pack_profiles(self.source_sha, self.profiles, self.artifact)
        metadata = json.loads((self.artifact / "metadata.json").read_text())
        del metadata["files"]["startup-prof.txt"]
        (self.artifact / "metadata.json").write_text(json.dumps(metadata))
        with self.assertRaisesRegex(ValueError, "digest mismatch"):
            apply_profile_artifact(self.artifact, self.root / "destination", self.source_sha)

    def measure_candidate_fixture(self, median):
        output = self.root / "gate"
        output.mkdir()
        profile = pack_profiles(self.source_sha, self.profiles, output / "candidate-profile")
        (output / "candidate.apk").write_bytes(b"release candidate")
        (output / "startup-test.apk").write_bytes(b"Partial Require harness")
        metadata = {"protocol": PROTOCOL, "candidateSha": self.source_sha, "baselineProfile": profile,
                    "candidateApkSha256": sha256(output / "candidate.apk"),
                    "testApkSha256": sha256(output / "startup-test.apk")}
        (output / "build-info.json").write_text(json.dumps(metadata))

        def adb_command(parts):
            parts = [str(part) for part in parts]
            if "getprop" in parts:
                return "29" if parts[-1] == "ro.build.version.sdk" else "test"
            if "settings" in parts:
                return "0"
            if "clear" in parts:
                return "Success"
            if "pull" in parts:
                report = {"benchmarks": [{"className": TEST_CLASS, "name": "coldStartup",
                                         "metrics": {METRIC: {"runs": [median] * 10}}}]}
                (Path(parts[-1]) / "test-benchmarkData.json").write_text(json.dumps(report))
            return ""

        args = Namespace(output=output, rounds=2, iterations=10, baseline_ms=100,
                         device="test-device", publish_baseline=True)
        instrument = Namespace(returncode=0, stdout="OK (1 test)", stderr="")
        with patch("startup_benchmark.command", side_effect=adb_command), \
                patch("startup_benchmark.subprocess.run", return_value=instrument), \
                redirect_stdout(io.StringIO()):
            measure_candidate(args)
        return output

    def test_passing_gate_persists_the_exact_profile_snapshot_and_protocol(self):
        output = self.measure_candidate_fixture(104.0)
        record = json.loads((output / "published-baseline/measurement.json").read_text())
        self.assertEqual(record["protocol"]["compilationMode"], "Partial")
        self.assertEqual(record["protocol"]["baselineProfileMode"], "Require")
        self.assertEqual(record["sourceSha"], self.source_sha)
        self.assertEqual(record["medianMs"], 104.0)
        self.assertEqual(record["baselineProfile"]["sourceSha"], self.source_sha)
        for name in PROFILE_FILES:
            self.assertEqual((output / "published-baseline/profiles" / name).read_bytes(),
                             (self.profiles / name).read_bytes())

    def test_regressing_gate_does_not_publish_a_new_measurement_or_profile(self):
        with self.assertRaisesRegex(ValueError, "more than 5%"):
            self.measure_candidate_fixture(106.0)
        self.assertFalse((self.root / "gate/published-baseline").exists())


if __name__ == "__main__":
    unittest.main()
