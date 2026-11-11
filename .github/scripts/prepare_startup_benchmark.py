"""Apply the same standalone startup harness to any application revision."""

import argparse
import json
from pathlib import Path
import shutil

PACKAGE = "com.d9tilov.moneymanager.benchmark"
SETTINGS = '''
if (providers.gradleProperty("startupBenchmark").orNull == "true") {
    include(":benchmark")
    dependencyResolutionManagement {
        versionCatalogs {
            create("startupBenchmark") {
                from(files("benchmark/libs.versions.toml"))
            }
        }
    }
}
'''
APP_CONFIG = '''
if (providers.gradleProperty("startupBenchmark").orNull == "true") {
    apply(from = rootProject.file("benchmark/target.gradle.kts"))
}
'''


def prepare(source, target):
    source, target = Path(source).resolve(), Path(target).resolve()
    if source != target:
        shutil.copytree(
            source / "benchmark", target / "benchmark", dirs_exist_ok=True,
            ignore=shutil.ignore_patterns("build", ".gradle"),
        )
    settings = target / "settings.gradle.kts"
    if 'include(":benchmark")' not in settings.read_text():
        settings.write_text(settings.read_text().rstrip() + "\n" + SETTINGS)
    app = target / "app/build.gradle.kts"
    if 'benchmark/target.gradle.kts' not in app.read_text():
        app.write_text(app.read_text().rstrip() + "\n" + APP_CONFIG)
    # Deliberately synthetic Firebase configuration. No production secrets or account.
    firebase = {
        "project_info": {
            "project_number": "123456789012", "project_id": "startup-benchmark",
            "storage_bucket": "startup-benchmark.appspot.com",
        },
        "client": [{
            "client_info": {
                "mobilesdk_app_id": "1:123456789012:android:0123456789abcdef012345",
                "android_client_info": {"package_name": PACKAGE},
            },
            "oauth_client": [{
                "client_id": "123456789012-startup.apps.googleusercontent.com", "client_type": 3,
            }],
            "api_key": [{"current_key": "AIzaSy" + "0" * 33}],
        }],
        "configuration_version": "1",
    }
    config = target / "app/src/benchmark/google-services.json"
    config.parent.mkdir(parents=True, exist_ok=True)
    config.write_text(json.dumps(firebase, indent=2) + "\n")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source", type=Path, default=Path(__file__).resolve().parents[2])
    parser.add_argument("--target", type=Path, required=True)
    args = parser.parse_args()
    prepare(args.source, args.target)


if __name__ == "__main__":
    main()
