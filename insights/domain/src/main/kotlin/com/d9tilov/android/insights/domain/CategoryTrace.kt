package com.d9tilov.android.insights.domain

typealias CategoryName = String

data class CategoryTrace(
    val days: List<DailyTotal>,
)
