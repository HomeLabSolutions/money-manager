package com.d9tilov.android.insights.data.impl.mapper

import com.d9tilov.android.insights.data.contract.model.InsightResponse
import com.d9tilov.android.insights.domain.model.GeneratedInsight

fun InsightResponse.toDomainModel(): GeneratedInsight = GeneratedInsight(text = text)
