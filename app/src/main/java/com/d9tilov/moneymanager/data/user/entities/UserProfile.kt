package com.d9tilov.moneymanager.data.user.entities

data class UserProfile(
    val clientId: String,
    val displayedName: String?,
    val firstName: String?,
    val secondName: String?,
    val pushNotificationSound: String?,
    val pushReportEnabled: Boolean,
    val hideTransactionDetailsInPushMessages: Boolean,
    val fiscalDay: Int,
    var budgetDayCreation: Long = 0L
)
