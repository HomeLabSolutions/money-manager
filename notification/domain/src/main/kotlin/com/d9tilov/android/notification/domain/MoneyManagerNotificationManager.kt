package com.d9tilov.android.notification.domain

interface MoneyManagerNotificationManager<T> {
    fun notify(
        id: Int,
        notification: T,
    )

    fun cancelAll()
}
