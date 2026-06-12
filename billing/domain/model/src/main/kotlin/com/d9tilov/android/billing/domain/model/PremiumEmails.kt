package com.d9tilov.android.billing.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PremiumEmails(
    val emails: List<String>,
)
