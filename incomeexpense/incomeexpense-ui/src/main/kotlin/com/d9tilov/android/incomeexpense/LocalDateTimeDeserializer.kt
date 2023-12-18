package com.d9tilov.android.incomeexpense

import com.d9tilov.android.core.utils.toLocalDateTime
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.LocalDateTime
import java.lang.reflect.Type

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
