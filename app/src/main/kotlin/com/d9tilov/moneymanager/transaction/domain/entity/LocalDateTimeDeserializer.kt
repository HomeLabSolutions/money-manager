package com.d9tilov.moneymanager.transaction.domain.entity

import com.d9tilov.moneymanager.core.util.toLocalDateTime
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.lang.reflect.Type
import java.time.ZoneId
import java.time.ZonedDateTime


class LocalDateTimeDeserializer: JsonDeserializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): LocalDateTime {
        val myType = json.asLong
        return myType.toLocalDateTime()
    }
}