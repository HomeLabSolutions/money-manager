package com.d9tilov.android.billing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PremiumEmails(
    val emails: List<String>,
)
