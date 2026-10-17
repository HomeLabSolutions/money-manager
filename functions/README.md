# Financial insights function

`generateInsight` is a Firebase callable function in `us-central1`. The Android app sends daily income and expense totals from the latest 90 calendar days in date-sorted category traces, separately by currency and transaction type. It also sends up to 90 previously shown insight texts to discourage repeated facts. A daily total can equal the amount of one transaction. The function does not receive transaction descriptions, notes, photos, locations, or a user ID in the model input.

The function requires Firebase Authentication. It uses the OpenAI Responses API and returns one insight as plain text. The Android app stores up to 90 recent results in its local Room database and passes them to the next request to discourage repeats. The Android app allows at most one saved insight per calendar day in the device's time zone for each local user. The function itself does not enforce a daily quota or store insight history.

## Firebase setup

1. Select the Firebase project used by the Android app: `firebase use <project-id>` or pass `--project <project-id>` to deployment commands.
2. In Firebase Remote Config, select the **Server** template and create a string parameter named `insights_base_prompt`. The function uses its built-in prompt only if the remote value is unavailable.
3. Set the API key in Cloud Secret Manager through Firebase CLI: `firebase functions:secrets:set OPENAI_API_KEY --project <project-id>`.
4. Deploy with `firebase deploy --only functions:generateInsight --project <project-id>`.

The API key is available only to this function. Do not place it in Remote Config, Android resources, or the repository. Before a production release, configure Firebase App Check and a usage limit for the OpenAI project to control abuse and spending. Update the Play Data safety form and privacy policy to reflect the data transfer.

Run `npm ci && npm test` in this directory to check the request contract locally. An end-to-end test needs a configured Firebase project and OpenAI key.

## Nearby expense places

Nearby places are requested directly from Geoapify by the Kotlin `NearbyPlacesDataRepo` through the Retrofit API in `core:network`. Firebase Functions and Firebase Authentication are not involved in this request.

Set `geoapify_api_key` in the ignored root `keystore.properties`, alongside `currency_api_key`. Gradle reads the key only from this file and exposes it as `BuildConfig.GEOAPIFY_API_KEY`. Builds without a key can compile, but cannot fetch places. The key is bundled in Android, following the currency API configuration. Geoapify uses the shared OkHttp client and debug logging, with apiKey redacted from logged request URLs.

The app sends saved expense coordinates only after tapping the pencil and requests named catering/retail places within 150 meters. Kotlin filters results to this radius, orders them by distance, removes duplicate IDs, and limits the list to 20. A missing distance is calculated from place coordinates. Opening a transaction reads its saved name and address from the database without an API request. The pencil becomes a loader while fetching; successful lists are cached for the lifetime of the ViewModel. A failed request can be retried on the next tap. The rounded exposed menu keeps its dividers and shows each distance below its address.

Choosing a place updates the draft name and address. The address appears below the selected name; both fields are stored by the existing Save button. Custom naming clears the previous address. Database version 3 adds both nullable fields in the single migration 2→3. Backup restoration requires the same database version as the installed app. A saved place enables the list item's location icon. Geoapify/OpenStreetMap attribution remains below the Google Maps view.

The former Firebase callable `getNearbyPlaces` and Secret Manager key `GEOAPIFY_API_KEY` are no longer used. `generateInsight` continues to use its own `OPENAI_API_KEY` secret. Update the privacy disclosure to include direct transfer of saved transaction coordinates to Geoapify.
