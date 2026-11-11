"""Bind generated Baseline and Startup Profiles to the release source revision."""

import argparse
import hashlib
import json
from pathlib import Path
import re
import shutil

PROFILE_FILES = ("baseline-prof.txt", "startup-prof.txt")


def file_digest(path):
    with path.open("rb") as stream:
        return hashlib.file_digest(stream, "sha256").hexdigest()


def pack_profiles(source_sha, profiles, output):
    if not re.fullmatch(r"[a-f0-9]{40}", source_sha):
        raise ValueError("Expected a full release source SHA")
    digests = {}
    for name in PROFILE_FILES:
        path = profiles / name
        if not path.is_file() or not path.stat().st_size:
            raise ValueError(f"Generated profile is missing or empty: {name}")
        digests[name] = file_digest(path)
    output.mkdir(parents=True, exist_ok=True)
    for name in PROFILE_FILES:
        shutil.copy2(profiles / name, output / name)
    metadata = {"schemaVersion": 1, "sourceSha": source_sha, "files": digests}
    (output / "metadata.json").write_text(json.dumps(metadata, indent=2) + "\n")
    return metadata


def apply_profile_artifact(artifact, destination, source_sha):
    metadata = json.loads((artifact / "metadata.json").read_text())
    if metadata.get("schemaVersion") != 1 or metadata.get("sourceSha") != source_sha:
        raise ValueError("Generated profile artifact does not match the release source SHA")
    for name in PROFILE_FILES:
        path = artifact / name
        if not path.is_file() or not path.stat().st_size or file_digest(path) != metadata["files"].get(name):
            raise ValueError(f"Generated profile digest mismatch: {name}")
    destination.mkdir(parents=True, exist_ok=True)
    for name in PROFILE_FILES:
        shutil.copy2(artifact / name, destination / name)
    return metadata


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source-sha", required=True)
    commands = parser.add_subparsers(dest="command", required=True)
    pack = commands.add_parser("pack")
    pack.add_argument("--profiles", required=True, type=Path)
    pack.add_argument("--output", required=True, type=Path)
    apply = commands.add_parser("apply")
    apply.add_argument("--artifact", required=True, type=Path)
    apply.add_argument("--destination", required=True, type=Path)
    args = parser.parse_args()
    try:
        if args.command == "pack":
            pack_profiles(args.source_sha, args.profiles, args.output)
        else:
            apply_profile_artifact(args.artifact, args.destination, args.source_sha)
    except (KeyError, OSError, TypeError, ValueError) as error:
        parser.exit(1, f"Invalid Baseline Profile artifact: {error}\n")


if __name__ == "__main__":
    main()
