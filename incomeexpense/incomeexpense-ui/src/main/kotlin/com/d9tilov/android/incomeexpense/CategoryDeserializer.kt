package com.d9tilov.android.incomeexpense

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.toLocalDateTime
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.LocalDateTime
import java.lang.reflect.Type

class CategoryDeserializer: JsonDeserializer<Category> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): Category {
        val obj = json.asJsonObject
//        System.out.println("moggot obj: $obj")
        val clientId = obj["clientId"].asString
        val color = obj["color"].asInt
        val icon = obj["icon"].asInt
        val name = obj["name"].asString
        val count = obj["usageCount"].asInt
        val id = obj["id"].asLong
        return Category(
            id = id,
            clientId = clientId,
            color = color,
            icon = icon,
            name = name,
            usageCount = count,
            type = TransactionType.INCOME,
            children = emptyList(),
            parent = null
        )
    }
}
