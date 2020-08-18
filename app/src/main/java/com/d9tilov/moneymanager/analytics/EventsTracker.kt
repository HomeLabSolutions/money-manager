package com.d9tilov.moneymanager.analytics

interface EventsTracker {
    fun logEvent(
        category: String,
        key: String,
        value: String
    )

    fun logEvent(
        category: String,
        action: String
    )

    fun logEvent(
        category: String,
        action: String,
        vars: Map<String?, String?>
    )

    fun logTimeDiff(
        category: String,
        variable: String,
        diff: Long
    )

    fun setUserProperty(
        name: String,
        value: String
    )
}
