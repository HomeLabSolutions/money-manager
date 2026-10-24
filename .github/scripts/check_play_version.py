"""Check a version code against Google Play without uploading or committing edits."""

import argparse
import base64
import json
import os
from pathlib import Path
import subprocess
import time
from urllib.error import HTTPError
from urllib.parse import urlencode
from urllib.request import Request, urlopen


TOKEN_URL = "https://oauth2.googleapis.com/token"
PLAY_URL = "https://androidpublisher.googleapis.com/androidpublisher/v3/applications/com.d9tilov.moneymanager/edits"


def encode_json(value):
    return base64.urlsafe_b64encode(json.dumps(value, separators=(",", ":")).encode()).rstrip(b"=")


def request_json(url, method="GET", data=None, token=None, content_type="application/json"):
    headers = {"Content-Type": content_type}
    if token:
        headers["Authorization"] = "Bearer " + token
    request = Request(url, data=data, headers=headers, method=method)
    with urlopen(request, timeout=30) as response:
        body = response.read()
    return json.loads(body) if body else {}


def sign_assertion(unsigned, private_key):
    reader, writer = os.pipe()
    try:
        os.write(writer, private_key.encode())
        os.close(writer)
        writer = None
        return subprocess.run(
            ["openssl", "dgst", "-sha256", "-sign", f"/dev/fd/{reader}"],
            input=unsigned, capture_output=True, pass_fds=(reader,), check=True,
        ).stdout
    finally:
        os.close(reader)
        if writer is not None:
            os.close(writer)


def access_token(credentials):
    if credentials.get("type") != "service_account":
        raise ValueError("PLAY_SERVICE_ACCOUNT_JSON must contain a service account JSON key.")
    now = int(time.time())
    claims = {
        "iss": credentials["client_email"],
        "scope": "https://www.googleapis.com/auth/androidpublisher",
        "aud": TOKEN_URL, "iat": now, "exp": now + 3600,
    }
    unsigned = encode_json({"alg": "RS256", "typ": "JWT"}) + b"." + encode_json(claims)
    signature = sign_assertion(unsigned, credentials["private_key"])
    assertion = unsigned + b"." + base64.urlsafe_b64encode(signature).rstrip(b"=")
    body = urlencode({"grant_type": "urn:ietf:params:oauth:grant-type:jwt-bearer", "assertion": assertion.decode()}).encode()
    return request_json(TOKEN_URL, "POST", body, content_type="application/x-www-form-urlencoded")["access_token"]


def check_available(token, version_code):
    edit = request_json(PLAY_URL, "POST", b"{}", token)["id"]
    try:
        versions = []
        for kind in ("bundles", "apks"):
            items = request_json(f"{PLAY_URL}/{edit}/{kind}", token=token).get(kind, [])
            versions.extend(int(item["versionCode"]) for item in items)
        tracks = request_json(f"{PLAY_URL}/{edit}/tracks", token=token).get("tracks", [])
        for track in tracks:
            for release in track.get("releases", []):
                versions.extend(int(code) for code in release.get("versionCodes", []))
        highest = max(versions, default=0)
        if version_code <= highest:
            raise ValueError(f"Calculated versionCode {version_code} must exceed Google Play's highest uploaded code {highest}.")
        return highest
    finally:
        request_json(f"{PLAY_URL}/{edit}", "DELETE", token=token)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("version_code", type=int)
    parser.add_argument("--credentials-file", type=Path)
    args = parser.parse_args()
    try:
        if not 1 <= args.version_code <= 2_100_000_000:
            raise ValueError("versionCode must be between 1 and 2100000000.")
        raw = args.credentials_file.read_text() if args.credentials_file else os.environ.get("PLAY_SERVICE_ACCOUNT_JSON", "")
        if not raw:
            raise ValueError("Configure the PLAY_SERVICE_ACCOUNT_JSON Actions secret.")
        highest = check_available(access_token(json.loads(raw)), args.version_code)
    except HTTPError as error:
        try:
            detail = json.loads(error.read()).get("error", {})
            message = detail.get("message") if isinstance(detail, dict) else detail
        except (ValueError, AttributeError):
            message = "Google API request failed."
        parser.exit(1, f"Google Play preflight failed (HTTP {error.code}): {message}\n")
    except (OSError, ValueError, KeyError, subprocess.CalledProcessError) as error:
        parser.exit(1, f"Google Play preflight failed: {error}\n")
    print(f"versionCode {args.version_code} is available; highest uploaded code is {highest}.")


if __name__ == "__main__":
    main()
