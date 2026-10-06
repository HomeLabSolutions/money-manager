package com.d9tilov.android.insights.domain

data class GeneratedInsight(
    val text: String,
)

fun InsightResponse.toDomainModel(): GeneratedInsight = GeneratedInsight(text = text)
