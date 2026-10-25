# Google Play internal delivery

The `Google Play internal testing` workflow accepts an app version such as `1.4.3.3`, calculates its version code using the existing project formula, and builds a signed release AAB. It runs manually from `develop`, checks the code against Google Play before building, and saves the version in `develop` through a version PR after successful build and CI checks. Uploading to Google Play is optional and targets the internal testing track.

The AAB and R8 mapping are compressed and retained for seven days. The workflow run and Google Play release names include the requested version; the Play release name also includes the calculated version code.

The existing Android CI workflow also retains debug APKs for seven days. Download them from a workflow run's **Artifacts** section. These artifacts belong to the public repository and are downloadable by signed-in users with read access. Only build outputs are uploaded.

## Prerequisites

- The app `com.d9tilov.moneymanager` exists in Play Console and has already had its first APK/AAB uploaded manually.
- Use the existing upload keystore registered for this app. The upload certificate must match **Play Console → App integrity → App signing → Upload key certificate**. [Android app signing](https://developer.android.com/studio/publish/app-signing) explains how upload keys differ from app signing keys.
- Complete the app's outstanding setup requirements in Play Console, and configure the internal testing testers and opt-in link.
- Choose a version whose calculated code is greater than every version already uploaded to Play. The workflow checks this automatically before creating a version commit or building.

## Set up Google Play API access

1. Select a Google Cloud project, such as the existing Firebase project, and enable the [Google Play Android Developer API](https://console.cloud.google.com/apis/library/androidpublisher.googleapis.com).
2. In **IAM & Admin → Service Accounts**, create an account such as `github-play-internal`. Publishing access is granted in Play Console; a Google Cloud project-wide Owner or Editor role is unnecessary.
3. In **Play Console → Users and permissions**, invite that service account's email. Grant access to **Money Manager** with **View app information (read-only)** and **Release apps to testing tracks** permissions.
4. In the service account's **Keys** tab, create and download a JSON key. Store its contents in the `PLAY_SERVICE_ACCOUNT_JSON` GitHub Actions secret below.

See Google's [API access guide](https://developers.google.com/android-publisher/getting_started) and [Play Console permissions](https://support.google.com/googleplay/android-developer/answer/9844686).

## Add repository secrets

Open [Settings → Secrets and variables → Actions](https://github.com/HomeLabSolutions/money-manager/settings/secrets/actions) and add these repository secrets:

| Secret | Value |
| --- | --- |
| `ANDROID_KEYSTORE_BASE64` | Base64 encoding of the existing upload `.jks`/`.keystore` file. |
| `ANDROID_KEYSTORE_PASSWORD` | Keystore password. |
| `ANDROID_KEY_ALIAS` | Alias of the upload key inside that keystore. |
| `ANDROID_KEY_PASSWORD` | Password for that key; it may equal the keystore password. |
| `MAPS_API_KEY` | Production Maps SDK for Android API key used by `maps_api_key`. Its Android restrictions must allow `com.d9tilov.moneymanager` with the Google Play **app signing** certificate. |
| `CURRENCY_API_KEY` | Exchange-rate API key from the local `currency_api_key` property. |
| `GEOCODING_API_KEY` | Geocoding API key from the local `geocoding_api_key` property. |
| `GEOAPIFY_API_KEY` | Nearby-place API key from the local `geoapify_api_key` property. |
| `GOOGLE_SERVICES_JSON` | Base64 encoding of the production `google-services.json`. This secret is already used by Android CI; keep it if it is current. |
| `PLAY_SERVICE_ACCOUNT_JSON` | Raw contents of the service account JSON key, without base64 encoding. Required for the version-availability check even in build-only mode. |
| `RELEASE_BOT_TOKEN` (optional) | Fine-grained GitHub PAT for `HomeLabSolutions/money-manager` with Contents and Pull requests read/write, and Actions read access. This lets ordinary PR CI start without the approval required for PRs created by `GITHUB_TOKEN`. |

On macOS, copy the base64 keystore directly to the clipboard, replacing the path:

```sh
base64 -i /absolute/path/to/upload-keystore.jks | tr -d '\n' | pbcopy
```

Alternatively, set it directly with GitHub CLI:

```sh
base64 -i /absolute/path/to/upload-keystore.jks | tr -d '\n' | gh secret set ANDROID_KEYSTORE_BASE64 --repo HomeLabSolutions/money-manager
gh secret set PLAY_SERVICE_ACCOUNT_JSON --repo HomeLabSolutions/money-manager < /absolute/path/to/service-account.json
```

For passwords, alias and the Maps API key, use the GitHub form or interactive `gh secret set SECRET_NAME --repo HomeLabSolutions/money-manager`. Values should not be put into tracked files or command-line arguments.

Signing passwords are read directly from environment variables during the CI build. The Gradle Secrets plugin ignores signing properties so they are not included in the app's generated `BuildConfig`. Existing local signing via `keystore.properties` remains available.

## Version format and repository updates

Enter `major.minor.patch.build`, for example `1.4.3.3`. The workflow updates the four existing version fields in the root `build.gradle.kts`; both the displayed `versionName` and the numeric `versionCode` then come from that committed configuration.

The existing formula is `1000 * (1000 * major + 100 * minor + patch) + build`: `1.4.3.3` becomes `1403003`. To keep this mapping unambiguous, minor must be 0–9, patch 0–99 and build 0–999. Leading zeroes, downgrades and codes outside Google's 1–2100000000 range are rejected.

Code `1403002` was already uploaded with display version `1.4.3.1` using the previous workflow. Consequently, `1.4.3.2` is reserved too; the first unused version for migration to version-name input was `1.4.3.3`. After that version is uploaded, choose a higher version such as `1.4.3.4`.

`develop` requires a PR and a successful `build` check. When the version changes, the workflow creates a dedicated branch and a PR named `Release app version 1.4.3.3`, then waits for its ordinary `pull_request` CI while building the release from the same immutable commit in parallel. After both succeed, it merges the version PR into `develop`; only then can it upload the checked AAB to Play.

By default the version PR is created with `GITHUB_TOKEN`. GitHub requires approval before running CI on such a PR: open the version PR and select **Approve workflows to run**. The delivery summary links to the PR, and the merge step waits for approval and CI completion. Merge remains automatic after successful PR checks.

For unattended releases, configure the optional `RELEASE_BOT_TOKEN` repository secret. Open the [pre-filled fine-grained token form](https://github.com/settings/personal-access-tokens/new?name=money-manager-release&target_name=HomeLabSolutions&expires_in=90&contents=write&pull_requests=write&actions=read), choose **Only select repositories → money-manager**, and generate the token. The form requests **Contents: Read and write**, **Pull requests: Read and write**, and **Actions: Read**, with a 90-day expiration. Save the generated value as `RELEASE_BOT_TOKEN` in repository Actions secrets and renew it before expiration. It is used only to push the version branch and create/read the version PR and CI; build and publish jobs keep their existing credentials.

The version PR follows the existing branch protection requirements. A successful `workflow_dispatch` run does **not** satisfy required PR checks, so the delivery waits specifically for a `pull_request` run on the expected commit. See [GitHub workflow triggering](https://docs.github.com/en/actions/how-tos/write-workflows/choose-when-workflows-run/trigger-a-workflow) and [required status checks](https://docs.github.com/en/pull-requests/how-tos/merge-and-close-pull-requests/troubleshooting-required-status-checks).

Build-only runs also save the version after successful checks. A later publishing run can reuse that repository version if its code has not yet been uploaded to Play. A failed build leaves its version PR open and does not publish. If `develop` moves during validation and GitHub blocks the version PR as out of date, the workflow fails before publishing; start a fresh run to build and validate the latest `develop`.

## Run a release

1. Merge the delivery workflow into the default branch (`develop`) so GitHub shows its **Run workflow** button.
2. Open **Actions → Google Play internal testing → Run workflow**, select `develop`, and enter the desired `version`, for example `1.4.3.3`. There is no manual version-code field.
3. Leave **Upload the signed AAB to Google Play internal testing** unchecked for the first run. The build and checks run without publishing. Download the signed AAB and mapping from the run's **Artifacts** section.
4. After checking the build, run the workflow with publishing enabled. If the earlier build was never uploaded to Play, reuse the same version; otherwise choose a new higher version. This run builds and checks `develop` again before uploading its artifact.
5. Verify the new release under **Play Console → Testing → Internal testing**, then install it using the tester opt-in link. The workflow uploads with `status: completed` to make the release available to internal testers, subject to Play Console requirements and processing.

Each upload must use an unused version code. Workflow reruns after a successful Play upload need a higher version. Concurrent delivery runs are serialized.

All signing, runtime API and service account secrets must be configured before starting a run.
