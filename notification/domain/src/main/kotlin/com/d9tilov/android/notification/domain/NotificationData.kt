package com.d9tilov.android.notification.domain

data class NotificationData(
    val channelId: String,
    val channelName: String,
    val title: String,
    val text: CharSequence,
)
