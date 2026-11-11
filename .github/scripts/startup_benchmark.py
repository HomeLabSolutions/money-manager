"""Build and compare two application revisions using an identical Macrobenchmark harness."""

import argparse
import hashlib
import io
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import tarfile

from compare_startup_benchmark import compare, markdown
from prepare_startup_benchmark import PACKAGE, prepare

ROOT = Path(__file__).resolve().parents[2]
TEST_PACKAGE = "com.d9tilov.moneymanager.startup"


def command(args, **kwargs):
    return subprocess.check_output([str(a) for a in args], text=True, timeout=120, **kwargs).strip()


def sha256(path):
    with Path(path).open("rb") as stream:
        return hashlib.file_digest(stream, "sha256").hexdigest()


def resolve(repository, ref):
    for candidate in [ref, f"origin/{ref}"]:
        result = subprocess.run([
            "git", "-C", str(repository), "rev-parse", "--verify", "--end-of-options",
            f"{candidate}^{{commit}}",
        ], capture_output=True, text=True, timeout=30)
        sha = result.stdout.strip()
        if result.returncode == 0 and re.fullmatch(r"[a-f0-9]{40}", sha):
            return sha
    raise ValueError(f"Could not resolve application revision: {ref}")


def archive(repository, sha, destination):
    if destination.exists():
        raise ValueError(f"Refusing to overwrite {destination}")
    destination.mkdir(parents=True)
    data = subprocess.check_output(["git", "-C", str(repository), "archive", sha], timeout=120)
    with tarfile.open(fileobj=io.BytesIO(data)) as source:
        source.extractall(destination, filter="data")
    prepare(ROOT, destination)


def build_apks(checkout, output, label):
    env = os.environ.copy()
    properties = ROOT / "local.properties"
    if not env.get("ANDROID_HOME") and properties.exists():
        for line in properties.read_text().splitlines():
            if line.startswith("sdk.dir="):
                env["ANDROID_HOME"] = line.partition("=")[2]
    log = output / f"build-{label}.log"
    with log.open("w") as stream:
        subprocess.run([
            "./gradlew", ":app:assembleBenchmark", ":benchmark:assembleBenchmark",
            "-PstartupBenchmark=true", "-x", ":app:uploadCrashlyticsMappingFileBenchmark",
            "--no-configuration-cache", "--console=plain", "--max-workers=4",
            "-Pkotlin.daemon.jvmargs=-Xmx6g",
        ], cwd=checkout, env=env, stdout=stream, stderr=subprocess.STDOUT,
            check=True, timeout=2400)
    apks = list((checkout / "app/build/outputs/apk/benchmark").glob("*.apk"))
    tests = list((checkout / "benchmark/build/outputs/apk/benchmark").glob("*.apk"))
    if len(apks) != 1 or len(tests) != 1:
        raise ValueError(f"Expected exactly one application APK and test APK in {checkout}")
    shutil.copy2(apks[0], output / f"{label}.apk")
    shutil.copy2(tests[0], output / "startup-test.apk")


def cached_baseline(sha, output):
    """Only reuse a successful develop push with matching source and APK digest."""
    if not os.environ.get("GH_TOKEN") or not os.environ.get("GITHUB_REPOSITORY"):
        return None
    try:
        runs = json.loads(command([
            "gh", "run", "list", "--repo", os.environ["GITHUB_REPOSITORY"],
            "--workflow", "android.yml", "--branch", "develop", "--event", "push",
            "--commit", sha, "--status", "success", "--limit", "5", "--json", "databaseId",
        ]))
        for run in runs:
            cache = output / f"cached-{run['databaseId']}"
            result = subprocess.run([
                "gh", "run", "download", str(run["databaseId"]), "--repo",
                os.environ["GITHUB_REPOSITORY"], "--name", "startup-baseline", "--dir", str(cache),
            ], capture_output=True, text=True, timeout=120)
            if result.returncode:
                continue
            metadata = json.loads((cache / "measurement.json").read_text())
            apk = cache / "app-benchmark.apk"
            if (metadata["sourceSha"] == sha and metadata.get("packageName") == PACKAGE and
                    metadata["apkSha256"] == sha256(apk)):
                return apk, metadata
    except (OSError, KeyError, ValueError, subprocess.SubprocessError):
        # A missing/expired artifact causes a real reference build, never a skipped gate.
        pass
    return None


def build(args):
    output = args.output.resolve()
    output.mkdir(parents=True, exist_ok=True)
    baseline_sha = resolve(args.repository, args.baseline_ref)
    candidate_sha = resolve(args.repository, args.candidate_ref)
    reference = cached_baseline(baseline_sha, output)
    if args.baseline_apk:
        shutil.copy2(args.baseline_apk, output / "baseline.apk")
        reference = None
    elif reference:
        shutil.copy2(reference[0], output / "baseline.apk")
    else:
        checkout = output / "sources/baseline"
        archive(args.repository, baseline_sha, checkout)
        build_apks(checkout, output, "baseline")
    checkout = output / "sources/candidate"
    archive(args.repository, candidate_sha, checkout)
    build_apks(checkout, output, "candidate")
    metadata = {
        "schemaVersion": 1, "baselineSha": baseline_sha, "candidateSha": candidate_sha,
        "baselineApkSha256": sha256(output / "baseline.apk"),
        "candidateApkSha256": sha256(output / "candidate.apk"),
        "testApkSha256": sha256(output / "startup-test.apk"),
        "previousMeasurement": reference[1] if reference else None,
    }
    (output / "build-info.json").write_text(json.dumps(metadata, indent=2) + "\n")
    print(json.dumps(metadata, indent=2), flush=True)


def measure(args):
    output = args.output.resolve()
    if args.rounds < 2 or args.iterations < 10:
        raise ValueError("Use at least two rounds and ten iterations per round")
    metadata = json.loads((output / "build-info.json").read_text())
    for label in ["baseline", "candidate"]:
        if sha256(output / f"{label}.apk") != metadata[f"{label}ApkSha256"]:
            raise ValueError(f"{label} APK changed after the build")
    if sha256(output / "startup-test.apk") != metadata["testApkSha256"]:
        raise ValueError("Macrobenchmark test APK changed after the build")
    adb = ["adb", "-s", args.device]

    def shell(*parts):
        return command(adb + ["shell", *parts])

    environment = {key: shell("getprop", prop) for key, prop in {
        "fingerprint": "ro.build.fingerprint", "api": "ro.build.version.sdk",
        "abi": "ro.product.cpu.abi", "model": "ro.product.model",
    }.items()}
    if int(environment["api"]) < 34:
        raise ValueError("API 34+ is required to preserve fixture state across compilation resets")
    wifi = shell("settings", "get", "global", "wifi_on")
    mobile = shell("settings", "get", "global", "mobile_data")
    reports = {"baseline": [], "candidate": []}
    subprocess.run(adb + ["uninstall", TEST_PACKAGE], capture_output=True, timeout=60)
    command(adb + ["install", "-r", "-t", output / "startup-test.apk"])
    try:
        shell("svc", "wifi", "disable")
        shell("svc", "data", "disable")
        shell("input", "keyevent", "KEYCODE_WAKEUP")
        shell("input", "keyevent", "KEYCODE_MENU")
        for round_index in range(args.rounds):
            order = ["baseline", "candidate"] if round_index % 2 == 0 else ["candidate", "baseline"]
            for label in order:
                destination = output / f"{label}-{round_index + 1}"
                destination.mkdir()
                remote = f"/sdcard/Android/media/{TEST_PACKAGE}/startup-{label}-{round_index + 1}"
                print(f"Measuring {label} round {round_index + 1}", flush=True)
                # Reinstall the disposable fixture so older versionCodes/signatures also work.
                subprocess.run(adb + ["uninstall", PACKAGE], capture_output=True, timeout=60)
                command(adb + ["install", "-r", output / f"{label}.apk"])
                if shell("pm", "clear", PACKAGE) != "Success":
                    raise ValueError("Could not reset the benchmark-only application fixture")
                shell("mkdir", "-p", remote)
                result = subprocess.run(adb + [
                    "shell", "am", "instrument", "-w", "-r",
                    "-e", "class", f"{TEST_PACKAGE}.StartupBenchmark#coldStartup",
                    "-e", "startupIterations", str(args.iterations),
                    "-e", "additionalTestOutputDir", remote,
                    "-e", "androidx.benchmark.suppressErrors", "EMULATOR",
                    "-e", "androidx.benchmark.killProcessDelayMillis", "1000",
                    "-e", "listener", "androidx.benchmark.junit4.SideEffectRunListener",
                    f"{TEST_PACKAGE}/androidx.test.runner.AndroidJUnitRunner",
                ], capture_output=True, text=True, timeout=1200)
                (destination / "instrumentation.log").write_text(result.stdout + result.stderr)
                failed = result.returncode or not re.search(r"OK \(1 test\)", result.stdout)
                if failed:
                    log = subprocess.run(adb + [
                        "logcat", "-d", "-t", "2000", "-s", "AndroidRuntime:E",
                        "MacrobenchmarkScope:D", "Benchmark:D",
                    ], capture_output=True, text=True, timeout=30)
                    (destination / "device-logcat.log").write_text(log.stdout + log.stderr)
                command(adb + ["pull", remote + "/.", destination])
                if failed:
                    raise ValueError(f"Instrumentation did not complete successfully: {destination}")
                matches = list(destination.rglob("*-benchmarkData.json"))
                if len(matches) != 1:
                    raise ValueError(f"Expected exactly one Macrobenchmark JSON: {destination}")
                reports[label].append(matches[0])
        result = compare(reports["baseline"], reports["candidate"])
        result.update(metadata)
        result["environment"] = environment
        result["protocol"] = {
            "startupMode": "COLD", "compilationMode": "Full", "rounds": args.rounds,
            "iterationsPerRound": args.iterations, "killProcessDelayMillis": 1000,
        }
        (output / "comparison.json").write_text(json.dumps(result, indent=2) + "\n")
        (output / "comparison.md").write_text(markdown(result))
        if args.publish_baseline and result["passed"]:
            baseline = output / "published-baseline"
            baseline.mkdir()
            shutil.copy2(output / "candidate.apk", baseline / "app-benchmark.apk")
            record = {
                "schemaVersion": 1, "sourceSha": metadata["candidateSha"],
                "packageName": PACKAGE,
                "apkSha256": metadata["candidateApkSha256"], "metric": result["metric"],
                "medianMs": result["candidate"]["medianMs"], "environment": environment,
            }
            (baseline / "measurement.json").write_text(json.dumps(record, indent=2) + "\n")
        print(markdown(result), flush=True)
        if not result["passed"]:
            raise ValueError("Startup median regressed by more than 5%")
    finally:
        # This package is a disposable benchmark fixture, never the user's app/data.
        subprocess.run(adb + ["uninstall", PACKAGE], capture_output=True, timeout=60)
        subprocess.run(adb + ["uninstall", TEST_PACKAGE], capture_output=True, timeout=60)
        if wifi == "1":
            shell("svc", "wifi", "enable")
        if mobile == "1":
            shell("svc", "data", "enable")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest="command", required=True)
    builder = commands.add_parser("build")
    builder.add_argument("--repository", type=Path, default=ROOT)
    builder.add_argument("--baseline-ref", required=True)
    builder.add_argument("--candidate-ref", required=True)
    builder.add_argument("--baseline-apk", type=Path)
    builder.add_argument("--output", type=Path, required=True)
    runner = commands.add_parser("measure")
    runner.add_argument("--device", required=True)
    runner.add_argument("--output", required=True, type=Path)
    runner.add_argument("--rounds", type=int, default=3)
    runner.add_argument("--iterations", type=int, default=15)
    runner.add_argument("--publish-baseline", action="store_true",
                        default=os.environ.get("PUBLISH_BASELINE") == "true")
    args = parser.parse_args()
    try:
        (build if args.command == "build" else measure)(args)
    except (OSError, KeyError, ValueError, subprocess.SubprocessError) as error:
        parser.exit(1, f"Startup benchmark failed: {error}\n")


if __name__ == "__main__":
    main()
