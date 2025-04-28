package com.d9tilov.android.statistics.ui.model

data class TransactionDetailsChartModel(
    val categoryId: Long,
    val from: Long,
    val to: Long,
    val inStatistics: Boolean,
)
