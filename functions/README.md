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

`getNearbyPlaces` is a separate authenticated callable function in `us-central1`. It receives the saved expense coordinates and a language code, and searches named catering and retail places within 150 meters using Geoapify. It returns up to 20 places ordered by distance. Coordinates are sent to Geoapify only after tapping the pencil in an expense editor; the app does not request a new location for this feature. Opening the editor reads the saved name from the local database without a places request. An expense without a name shows a Place not selected placeholder. The first pencil tap replaces it with a loading indicator, fetches the list, and opens a rounded exposed dropdown menu anchored to the pencil when loading finishes. Successful results, including empty lists, are cached in the screen's ViewModel; subsequent taps reopen the menu with the cached list without another request or loader. Failed requests can be retried on the next tap. A new ViewModel starts with an empty cache and waits for a pencil tap before requesting places. Choosing a result or entering a custom name changes only the draft; the name is stored locally when the transaction is saved. Attribution appears below the Google Maps view. A saved place adds a location marker to its transaction list item.

Set the key with `npx --yes firebase-tools functions:secrets:set GEOAPIFY_API_KEY --project money-manager-679a2`. The key is stored in Cloud Secret Manager, like `OPENAI_API_KEY`, and is bound only to `getNearbyPlaces`. It is not exposed through Remote Config or bundled in Android. Deploy with `npx --yes firebase-tools deploy --only functions:getNearbyPlaces --project money-manager-679a2` after adding the secret.

The Geoapify free quota covers Geoapify API usage; Firebase hosting and function execution retain their own billing terms. Geoapify and OpenStreetMap attribution is shown alongside service results in Android. Update the privacy disclosure to include transfer of saved transaction coordinates to Firebase and Geoapify when this feature is released.
