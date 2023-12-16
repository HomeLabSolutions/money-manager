package com.d9tilov.moneymanager.transaction.domain.entity

import com.d9tilov.moneymanager.core.util.toMillis
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.lang.reflect.Type

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {
    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        return context.serialize(src.toMillis())
    }
}
