package com.d9tilov.android.insights.domain.model

enum class InsightGenerationStatus {
    UNDEFINED,
    LOADING,
    AVAILABLE,
    INSUFFICIENT_DATA,
    DAILY_LIMIT_REACHED,
}
