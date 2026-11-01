# Spending widget

The home screen widget shows the same three expense summary values as the app:

- Available to spend today (or overspending when the amount is negative).
- Spent today.
- Spent in the current fiscal period, based on the profile's fiscal day.

All amounts use the main currency and the existing `TransactionInteractor` calculations.
The widget follows the same Premium availability as the in-app summary.

After installing the app, open the launcher's widget picker and add **Money Manager → Spending**
(**Расходы** in Russian). Tap the widget to open the app. The compact layout defaults to 2 × 1 cells
and shows a compact title and three labeled rows without a refresh button. Labels reuse the home screen
string resources and wrap onto two lines when needed. Its minimum content size is 180 × 96 dp.
The widget can be resized, supports light and dark themes, and uses system colors on Android 12 and later.

The widget refreshes when it is added, when the app leaves the foreground, when transaction,
currency, or fiscal-day data changes while the app process is alive, and approximately every
30 minutes through WorkManager. Android may delay background work to save battery. A fresh snapshot
recreates all date-bounded flows so the day and fiscal period roll over even if the app is closed.
Date, time zone, locale, and package update broadcasts also request a fresh snapshot when delivered.
Removing the last widget cancels its background work.

The widget displays a sign-in or setup message before the account is ready. Logging out clears its
amounts immediately; a background refresh checks the account again before publishing a snapshot.
If data cannot be read, it clears the amounts and offers to open the app instead of showing zero or stale values.

## Verification

Run `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:connectedDebugAndroidTest :app:lintDebug detektCheck ktlintCheck`.

The Android tests render real RemoteViews with fixture amounts in English and Russian, light and
dark themes, and verify negative amounts, clearing amounts after sign-out, and minimum-size layout.
They save widget previews under the app's external files directory in `widget-test-previews/`.

On an emulator or device, add the widget and compare all three amounts with the home screen.
Add, edit, and delete a transaction, then return to the launcher and check the refreshed amounts.
Also check a negative daily allowance, a different main currency and fiscal day, Russian and English,
both themes, resizing, automatic refresh, and opening the app. Log out and confirm that the amounts disappear.
Add two widget instances, then remove them one at a time to verify that updates stop only after the last removal.
