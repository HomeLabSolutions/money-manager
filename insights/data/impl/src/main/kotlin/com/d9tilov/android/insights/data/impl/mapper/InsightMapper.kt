package com.d9tilov.android.insights.data.impl.mapper

import com.d9tilov.android.database.entity.InsightDbModel
import com.d9tilov.android.insights.domain.model.Insight

internal fun Insight.toDbModel(clientId: String): InsightDbModel =
    InsightDbModel(
        id = id,
        clientId = clientId,
        text = text,
        createdDate = createdAtMillis,
    )

internal fun InsightDbModel.toDomainModel(): Insight =
    Insight(
        id = id,
        createdAtMillis = createdDate,
        text = text,
    )
