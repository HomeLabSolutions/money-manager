package com.d9tilov.android.insights.data.mapper

import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.database.entity.InsightDbModel
import com.d9tilov.android.insights.domain.Insight

internal fun Insight.toDbModel(clientId: String): InsightDbModel =
    InsightDbModel(
        id = id,
        clientId = clientId,
        text = text,
        createdDate = createdAtMillis.toLocalDateTime(),
    )

internal fun InsightDbModel.toDomainModel(): Insight =
    Insight(
        id = id,
        createdAtMillis = createdDate.toMillis(),
        text = text,
    )
