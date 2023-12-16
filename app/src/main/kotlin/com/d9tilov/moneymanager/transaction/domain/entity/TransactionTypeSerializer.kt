package com.d9tilov.moneymanager.transaction.domain.entity

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class TransactionTypeSerializer : JsonSerializer<TransactionType> {
    override fun serialize(
        src: TransactionType,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        return when(src) {
            TransactionType.INCOME -> context.serialize(src)
            TransactionType.EXPENSE -> context.serialize(src)
        }
    }
}
