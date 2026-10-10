package com.d9tilov.android.insights.domain.model

typealias CategoryName = String

data class CategoryTrace(
    val days: List<DailyTotal>,
)
