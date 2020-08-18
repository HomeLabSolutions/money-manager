package com.d9tilov

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.d9tilov.moneymanager.analytics.EventsTracker
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class FirebaseEventsTracker(
    context: Application,
    private val preferencesStore: PreferencesStore
) : EventsTracker, ActivityLifecycleCallbacks {

    private val tracker: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    init {
        context.registerActivityLifecycleCallbacks(this)
    }

    override fun logEvent(category: String, key: String, value: String) {
        tracker.apply {
            setUserId(preferencesStore.uid)
            logEvent(category) {
                param(key, value)
            }
        }
    }

    override fun logEvent(category: String, action: String) {
        tracker.apply {
            setUserId(preferencesStore.uid)
            logEvent(category + "_" + action, null)
        }
    }

    override fun logEvent(
        category: String,
        action: String,
        vars: Map<String?, String?>
    ) {
        tracker.apply {
            val bundle = Bundle()
            vars.forEach {
                bundle.putString(it.key, it.value)
            }
            setUserId(preferencesStore.uid)
            logEvent(category + "_" + action, bundle)
        }
    }

    override fun logTimeDiff(
        category: String,
        variable: String,
        diff: Long
    ) {
        tracker.apply {
            val bundle = Bundle()
            bundle.putLong(variable, diff)
            setUserId(preferencesStore.uid)
            logEvent(category, bundle)
        }
    }

    override fun setUserProperty(name: String, value: String) {
        tracker.setUserProperty(name, value)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity) {
        tracker.apply {
            setCurrentScreen(activity, activity.localClassName, null)
            logScreen()
        }
    }

    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    private fun logScreen() {
        tracker.apply {
            setUserId(preferencesStore.uid)
            logEvent(FirebaseAnalytics.Event.SELECT_ITEM, Bundle())
        }
    }
}
