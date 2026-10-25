"""Wait for a real PR check, including any required workflow approval."""

import argparse
import json
import subprocess
import time


def run_status(run_id):
    result = subprocess.run(
        ["gh", "run", "view", str(run_id), "--json", "event,headSha,status,conclusion,url"],
        capture_output=True, text=True, check=True, timeout=30,
    )
    return json.loads(result.stdout)


def wait_for_ci(run_id, source_sha, timeout=1500, interval=30):
    deadline = time.monotonic() + timeout
    previous = None
    while time.monotonic() < deadline:
        run = run_status(run_id)
        if run["event"] != "pull_request":
            raise ValueError("Required PR checks must come from a pull_request run; workflow_dispatch does not count.")
        if run["headSha"] != source_sha:
            raise ValueError("CI belongs to another commit; refusing to merge the version PR.")
        state = (run["status"], run["conclusion"])
        if state != previous:
            if run["conclusion"] == "action_required":
                print(f"::notice::Approve workflows to run on the version PR: {run['url']}", flush=True)
            else:
                print(f"PR CI: {run['status']} {run['conclusion']} ({run['url']})", flush=True)
            previous = state
        if run["status"] == "completed" and run["conclusion"] != "action_required":
            if run["conclusion"] == "success":
                return
            raise ValueError(f"Required PR CI finished with {run['conclusion']}; version will not be merged.")
        time.sleep(interval)
    raise ValueError("Timed out waiting for PR CI. Approve workflows on the version PR if approval is required.")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("run_id", type=int)
    parser.add_argument("source_sha")
    args = parser.parse_args()
    try:
        wait_for_ci(args.run_id, args.source_sha)
    except (KeyError, OSError, ValueError, subprocess.SubprocessError) as error:
        parser.exit(1, f"PR CI validation failed: {error}\n")


if __name__ == "__main__":
    main()
