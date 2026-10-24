"""Validate a release version and update the existing Gradle version fields."""

import argparse
from dataclasses import dataclass
from pathlib import Path
import re


FIELDS = ("versionMajor", "versionMinor", "versionPatch", "versionBuild")
MAX_VERSION_CODE = 2_100_000_000


@dataclass(frozen=True)
class Version:
    parts: tuple[int, int, int, int]

    @property
    def name(self):
        return ".".join(map(str, self.parts))

    @property
    def code(self):
        major, minor, patch, build = self.parts
        return 1000 * (1000 * major + 100 * minor + patch) + build


def parse_version(value):
    if not re.fullmatch(r"(?:0|[1-9][0-9]{0,3})(?:\.(?:0|[1-9][0-9]{0,3})){3}", value):
        raise ValueError("Version must have four numeric parts, for example 1.4.3.3, without leading zeroes.")
    version = Version(tuple(map(int, value.split("."))))
    _, minor, patch, build = version.parts
    if minor > 9 or patch > 99 or build > 999:
        raise ValueError("The existing version-code scheme requires minor <= 9, patch <= 99 and build <= 999.")
    if not 1 <= version.code <= MAX_VERSION_CODE:
        raise ValueError("Calculated versionCode must be between 1 and 2100000000.")
    return version


def field_pattern(field):
    return rf'^(extra\["{field}"\][ \t]*=[ \t]*)([0-9]+)[ \t]*$'


def update_version(path, requested):
    text = path.read_text()
    parts = []
    for field in FIELDS:
        matches = list(re.finditer(field_pattern(field), text, re.MULTILINE))
        if len(matches) != 1:
            raise ValueError(f"Expected exactly one {field} field in build.gradle.kts.")
        parts.append(int(matches[0].group(2)))
    current = parse_version(".".join(map(str, parts)))
    if requested.code < current.code:
        raise ValueError(f"Requested version must be at least the repository version {current.name}.")
    changed = requested != current
    if changed:
        for field, value in zip(FIELDS, requested.parts):
            text = re.sub(field_pattern(field), lambda match: f"{match[1]}{value}", text, flags=re.MULTILINE)
        path.write_text(text)
    return changed


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("version")
    parser.add_argument("--build-file", type=Path, default=Path("build.gradle.kts"))
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    try:
        version = parse_version(args.version)
        changed = update_version(args.build_file, version)
    except (OSError, ValueError) as error:
        parser.exit(1, f"Release version validation failed: {error}\n")
    if args.output:
        with args.output.open("a") as output:
            output.write(f"version_name={version.name}\nversion_code={version.code}\nchanged={str(changed).lower()}\n")
    print(f"Version {version.name}; versionCode {version.code}; Gradle version changed: {changed}")


if __name__ == "__main__":
    main()
