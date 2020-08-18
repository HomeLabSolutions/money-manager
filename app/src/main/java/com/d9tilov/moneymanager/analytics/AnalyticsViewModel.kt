package com.d9tilov.moneymanager.analytics

import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel

abstract class AnalyticsViewModel<T : BaseNavigator>(protected var eventsTracker: EventsTracker) :
    BaseViewModel<T>() {

    protected fun logEvent(category: String, action: String) {
        eventsTracker.logEvent(category, action)
    }

    protected fun logEvent(category: String, key: String, value: String) {
        eventsTracker.logEvent(category, key, value)
    }
}
