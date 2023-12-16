package com.d9tilov.moneymanager.transaction.domain.entity

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class TransactionTypeDeserializer: JsonDeserializer<TransactionType> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): TransactionType {
        val myType = json.asJsonObject["name"].asString
        return when (myType) {
            "Income" -> context.deserialize(json, TransactionType.INCOME::class.java)
            "Expense" -> context.deserialize(json, TransactionType.EXPENSE::class.java)
            else -> throw IllegalArgumentException("not found: $myType")
        }
    }
}