# Google Play internal delivery

The `Google Play internal testing` workflow builds a signed release AAB, runs unit tests, ktlint, detekt and release lint, and retains the AAB and R8 mapping for seven days. It runs manually from `develop` or `master`. Uploading to Google Play is optional and targets the internal testing track.

The existing Android CI workflow also retains debug APKs for seven days. Download them from a workflow run's **Artifacts** section. These artifacts belong to the public repository and are downloadable by signed-in users with read access. Only build outputs are uploaded.

## Prerequisites

- The app `com.d9tilov.moneymanager` exists in Play Console and has already had its first APK/AAB uploaded manually.
- Use the existing upload keystore registered for this app. The upload certificate must match **Play Console → App integrity → App signing → Upload key certificate**. [Android app signing](https://developer.android.com/studio/publish/app-signing) explains how upload keys differ from app signing keys.
- Complete the app's outstanding setup requirements in Play Console, and configure the internal testing testers and opt-in link.
- Check the highest `versionCode` already uploaded in **App bundle explorer**, including testing tracks, before choosing a new value.

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
| `PLAY_SERVICE_ACCOUNT_JSON` | Raw contents of the service account JSON key, without base64 encoding. Only needed when uploading to Google Play. |

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

## First run

1. Merge the delivery workflow into the default branch (`develop`) so GitHub shows its **Run workflow** button.
2. Open **Actions → Google Play internal testing → Run workflow**, select `develop`, and enter a `version_code` greater than the highest number already uploaded to Play. Valid values are 1 through 2100000000. The app's displayed `versionName` comes from the root build configuration.
3. Leave **Upload the signed AAB to Google Play internal testing** unchecked for the first run. The build and checks run without publishing. Download the signed AAB and mapping from the run's **Artifacts** section.
4. After checking the build, run the workflow with publishing enabled. If the earlier build was never uploaded to Play, the same version code may be reused; otherwise choose a new higher number. This run builds and checks the selected branch again before uploading its artifact.
5. Verify the new release under **Play Console → Testing → Internal testing**, then install it using the tester opt-in link. The workflow uploads with `status: completed` to make the release available to internal testers, subject to Play Console requirements and processing.

Each upload must use an unused version code. Workflow reruns after a successful Play upload also need a new code. Concurrent delivery runs are serialized.

Google Play delivery cannot be verified end to end until the upload key, service account access and secrets are configured. Build-only runs can be used before configuring the service account secret.
