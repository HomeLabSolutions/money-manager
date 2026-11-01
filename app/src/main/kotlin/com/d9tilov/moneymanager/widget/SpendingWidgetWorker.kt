package com.d9tilov.moneymanager.widget

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.d9tilov.android.datastore.PreferencesStore
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import timber.log.Timber

@HiltWorker
class SpendingWidgetWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted parameters: WorkerParameters,
    private val dataSource: SpendingWidgetDataSource,
    private val preferences: PreferencesStore,
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        if (!SpendingWidgetProvider.hasWidgets(context)) return Result.success()
        val state =
            flow {
                emit(withTimeout(LOAD_TIMEOUT_MILLIS) { loadState() })
            }.catch { error ->
                Timber.w(error, "Unable to update spending widget")
                emit(SpendingWidgetState.Unavailable)
            }.first()
        currentCoroutineContext().ensureActive()
        SpendingWidgetViews.update(context, state)
        return if (state == SpendingWidgetState.Unavailable) Result.retry() else Result.success()
    }

    private suspend fun loadState(): SpendingWidgetState {
        val uid = currentUid()
        val state = dataSource.load(uid)
        // Never publish a snapshot from an account that has since signed out or changed.
        return if (uid == currentUid()) state else SpendingWidgetState.SignedOut
    }

    private suspend fun currentUid(): String? =
        preferences.uid.first().takeIf { uid -> uid == FirebaseAuth.getInstance().currentUser?.uid }

    companion object {
        private const val LOAD_TIMEOUT_MILLIS = 20_000L
    }
}
