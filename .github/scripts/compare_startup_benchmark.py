"""Compare cold-start TTID samples; fail when the median increases by more than 5%."""

import argparse
from decimal import Decimal
import json
import math
from pathlib import Path
import statistics

METRIC = "timeToInitialDisplayMs"
TEST_CLASS = "com.d9tilov.moneymanager.startup.StartupBenchmark"
MIN_SAMPLES = 10
MAX_REGRESSION = Decimal("5")


def read_samples(path):
    report = json.loads(Path(path).read_text())
    matches = [b for b in report["benchmarks"]
               if b.get("className") == TEST_CLASS and b.get("name") == "coldStartup"]
    if len(matches) != 1:
        raise ValueError(f"{path}: expected exactly one coldStartup benchmark")
    samples = matches[0]["metrics"][METRIC]["runs"]
    if len(samples) < MIN_SAMPLES:
        raise ValueError(f"{path}: expected at least {MIN_SAMPLES} samples")
    if any(isinstance(v, bool) or not isinstance(v, (int, float)) or
           not math.isfinite(v) or v <= 0 for v in samples):
        raise ValueError(f"{path}: startup samples must be finite positive milliseconds")
    return samples


def summarize(paths):
    samples = [sample for path in paths for sample in read_samples(path)]
    if not samples:
        raise ValueError("No startup reports supplied")
    return {
        "medianMs": statistics.median(samples), "minMs": min(samples), "maxMs": max(samples),
        "sampleCount": len(samples), "runsMs": samples,
    }


def compare(baseline_paths, candidate_paths):
    baseline = summarize(baseline_paths)
    candidate = summarize(candidate_paths)
    if baseline["sampleCount"] != candidate["sampleCount"]:
        raise ValueError("Baseline and candidate must have the same number of samples")
    before = Decimal(str(baseline["medianMs"]))
    after = Decimal(str(candidate["medianMs"]))
    regression = (after / before - 1) * 100
    return {
        "schemaVersion": 1, "metric": METRIC, "maxRegressionPercent": float(MAX_REGRESSION),
        "passed": after <= before * (1 + MAX_REGRESSION / 100),
        "regressionPercent": float(regression), "baseline": baseline, "candidate": candidate,
    }


def markdown(result):
    baseline, candidate = result["baseline"], result["candidate"]
    status = "PASS" if result["passed"] else "FAIL"
    return (
        f"## Startup regression: {status}\n\n"
        "Cold process start to initial display (TTID), fully compiled non-debuggable R8 APKs. "
        "Signed-out fixture, network disabled; both APKs measured on the same device.\n\n"
        "| Version | Median, ms | Samples |\n|---|---:|---:|\n"
        f"| Previous | {baseline['medianMs']:.3f} | {baseline['sampleCount']} |\n"
        f"| Candidate | {candidate['medianMs']:.3f} | {candidate['sampleCount']} |\n\n"
        f"Change: **{result['regressionPercent']:+.2f}%**. "
        f"Maximum permitted increase: **{result['maxRegressionPercent']:g}%**.\n\n"
        "The previous APK is remeasured to reduce differences between hosted runners. "
        "Emulator results are a relative CI signal, not end-user device latency.\n"
    )


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--baseline", nargs="+", required=True, type=Path)
    parser.add_argument("--candidate", nargs="+", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()
    try:
        result = compare(args.baseline, args.candidate)
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(result, indent=2) + "\n")
        print(markdown(result))
    except (KeyError, OSError, TypeError, ValueError) as error:
        parser.exit(2, f"Invalid startup benchmark: {error}\n")
    if not result["passed"]:
        parser.exit(1, "Startup median regressed by more than 5%.\n")


if __name__ == "__main__":
    main()
