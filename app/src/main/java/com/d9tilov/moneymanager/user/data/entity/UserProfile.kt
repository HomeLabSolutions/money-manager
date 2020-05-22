package com.d9tilov.moneymanager.user.data.entity

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val firstName: String?,
    val lastName: String?,
    val budgetDayCreation: Long = 0L
)
