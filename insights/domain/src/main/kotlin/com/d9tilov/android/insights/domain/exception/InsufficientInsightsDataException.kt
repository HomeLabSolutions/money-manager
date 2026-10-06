package com.d9tilov.android.insights.domain.exception

class InsufficientInsightsDataException :
    IllegalStateException("Insights require at least two weeks of transaction history")
