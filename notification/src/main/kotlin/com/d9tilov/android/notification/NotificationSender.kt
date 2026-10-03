package com.d9tilov.android.notification

interface NotificationSender<T> {
    fun send(data: T)
}
