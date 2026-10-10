package com.d9tilov.android.insights.domain.model

data class Insight(
    val id: Long,
    val createdAtMillis: Long,
    val text: String,
)
