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
