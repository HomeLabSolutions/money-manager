"""Select a compatible published startup measurement or remeasure its source revision."""

import argparse
import json
import math
from pathlib import Path
import re

from compare_startup_benchmark import METRIC
from prepare_startup_benchmark import PACKAGE

PROTOCOL = {
    "startupMode": "COLD", "compilationMode": "Partial",
    "baselineProfileMode": "Require", "scenario": "signed-out",
}


def positive_milliseconds(value):
    if isinstance(value, bool):
        raise ValueError("The startup baseline must be finite positive milliseconds")
    number = float(value)
    if not math.isfinite(number) or number <= 0:
        raise ValueError("The startup baseline must be finite positive milliseconds")
    return number


def select_baseline(record, initial_baseline_ms=None):
    if not isinstance(record, dict):
        raise ValueError("The startup baseline must be a JSON object")
    if record:
        if record.get("packageName") != PACKAGE or record.get("metric") != METRIC:
            raise ValueError("The stored startup baseline has the wrong package or metric")
        protocol = record.get("protocol", {})
        if not isinstance(protocol, dict):
            raise ValueError("The stored startup protocol must be a JSON object")
        if all(protocol.get(key) == value for key, value in PROTOCOL.items()):
            return {"baseline_ms": str(positive_milliseconds(record["medianMs"])),
                    "baseline_ref": "", "measure_command": "measure-candidate"}
        source = record.get("sourceSha", "")
        if not isinstance(source, str) or not re.fullmatch(r"[a-f0-9]{40}", source):
            raise ValueError("Incompatible startup baseline has no published source SHA to remeasure")
        return {"baseline_ms": "", "baseline_ref": source, "measure_command": "measure"}
    if initial_baseline_ms not in (None, ""):
        return {"baseline_ms": str(positive_milliseconds(initial_baseline_ms)),
                "baseline_ref": "", "measure_command": "measure-candidate"}
    raise ValueError("No published startup baseline. Supply a Partial/Require baseline_ms once.")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--record", required=True, type=Path)
    parser.add_argument("--initial-baseline-ms", default="")
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()
    try:
        selected = select_baseline(json.loads(args.record.read_text()), args.initial_baseline_ms)
        with args.output.open("a") as stream:
            stream.writelines(f"{key}={value}\n" for key, value in selected.items())
        if selected["baseline_ref"]:
            print("Stored compilation protocol differs; remeasuring the published source with Partial/Require.")
        else:
            print("Using the published Partial/Require startup median.")
    except (KeyError, OSError, TypeError, ValueError) as error:
        parser.exit(1, f"Invalid startup baseline: {error}\n")


if __name__ == "__main__":
    main()
