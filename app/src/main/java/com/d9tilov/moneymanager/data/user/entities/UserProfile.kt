package com.d9tilov.moneymanager.data.user.entities

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val firstName: String?,
    val secondName: String?,
    var budgetDayCreation: Long = 0L
)
