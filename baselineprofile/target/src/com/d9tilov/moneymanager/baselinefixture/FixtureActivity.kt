package com.d9tilov.moneymanager.baselinefixture

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d9tilov.android.core.constants.DataConstants.DATABASE_NAME
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.entity.CategoryDbModel
import com.d9tilov.android.database.entity.CurrencyDbModel
import com.d9tilov.android.database.entity.MainCurrencyDbModel
import com.d9tilov.android.database.entity.UserDbModel
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.user.domain.model.InsightLanguage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.math.BigDecimal

private const val FIXTURE_CURRENCY = "USD"
private const val FIXTURE_TAG = "StartupFixture"

class FixtureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        check(packageName.endsWith(".baselineprofile"))
        lifecycleScope.launch {
            runCatching { prepareSignedInScenario() }
                .onSuccess { showResult("baseline-fixture-ready", "Ready") }
                .onFailure { error ->
                    Log.e(FIXTURE_TAG, "Startup fixture preparation failed", error)
                    showResult("baseline-fixture-error", error.toString())
                }
        }
    }

    private suspend fun prepareSignedInScenario() {
        Log.i(FIXTURE_TAG, "Resetting startup fixture")
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val preferences = PreferencesStore(applicationContext)
        preferences.clearAllData()
        withContext(Dispatchers.IO) { applicationContext.deleteDatabase(DATABASE_NAME) }
        prepareSignedInUser(auth, preferences)
    }

    private suspend fun prepareSignedInUser(
        auth: FirebaseAuth,
        preferences: PreferencesStore,
    ) {
        val user = requireNotNull(auth.signInAnonymously().await().user)
        Log.i(FIXTURE_TAG, "Auth Emulator sign-in completed; seeding database")
        seedDatabase(user.uid)
        Log.i(FIXTURE_TAG, "Database seeded; saving fixture user")
        preferences.updateUid(user.uid)
        Log.i(FIXTURE_TAG, "Startup fixture ready")
    }

    private suspend fun seedDatabase(uid: String) =
        withContext(Dispatchers.IO) {
            val database =
                Room
                    .databaseBuilder(applicationContext, AppDatabase::class.java, DATABASE_NAME)
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .build()
            try {
                database.userDao().insert(
                    UserDbModel(
                        uid = uid,
                        firstName = "Startup",
                        lastName = "Fixture",
                        photoUrl = null,
                        showPrepopulate = false,
                        fiscalDay = 1,
                        insightLanguage = InsightLanguage.SYSTEM.tag,
                    ),
                )
                database.currencyDao().insert(listOf(CurrencyDbModel(FIXTURE_CURRENCY, "$", BigDecimal.ONE, 0)))
                database.mainCurrencyDao().insert(MainCurrencyDbModel(clientId = uid, code = FIXTURE_CURRENCY))
                seedCategories(database, uid)
            } finally {
                database.close()
            }
        }

    private suspend fun seedCategories(
        database: AppDatabase,
        uid: String,
    ) {
        listOf(TransactionType.EXPENSE, TransactionType.INCOME).forEachIndexed { index, type ->
            database.categoryDao().create(
                CategoryDbModel(
                    id = index + 1L,
                    uid = uid,
                    parentId = NO_ID,
                    type = type.value,
                    name = "Sample ${type.name}",
                    iconNameOrdinal = 0,
                    colorNameOrdinal = 0,
                    usageCount = 0,
                ),
            )
        }
    }

    private fun showResult(
        description: String,
        message: String,
    ) {
        setContentView(
            TextView(this).apply {
                contentDescription = description
                text = message
            },
        )
    }
}
