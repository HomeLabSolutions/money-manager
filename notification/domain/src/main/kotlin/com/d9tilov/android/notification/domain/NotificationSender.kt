package com.d9tilov.android.notification.domain

interface NotificationSender<T> {
    fun send(data: T)
}
