package com.d9tilov.moneymanager.category.data.local.mappers

import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun toDataModel(model: CategoryDbModel, parentModel: CategoryDbModel? = null) = with(model) {
        parentModel?.let {
            Category(
                id = id,
                clientId = uid,
                name = name,
                parent = toDataParentModel(parentModel),
                type = type,
                icon = icon,
                color = color,
                priority = priority,
                ordinal = ordinal
            )
        } ?: Category(
            id = id,
            clientId = uid,
            name = name,
            type = type,
            icon = icon,
            color = color,
            priority = priority,
            ordinal = ordinal
        )
    }

    fun toDataParentModel(model: CategoryDbModel) =
        with(model) {
            Category(
                id = id,
                clientId = uid,
                name = name,
                type = type,
                icon = icon,
                color = color,
                priority = priority,
                ordinal = ordinal
            )
        }

    fun toDbModel(category: Category) =
        with(category) {
            CategoryDbModel(
                id = id,
                uid = clientId,
                parentId = parent?.id ?: NO_ID,
                type = type,
                name = name,
                icon = icon,
                color = color,
                priority = priority,
                ordinal = ordinal
            )
        }

    fun toDataModelFromPrePopulate(model: PrepopulateCategory) =
        with(model) {
            CategoryDbModel(
                id = id,
                uid = clientId,
                parentId = parentId,
                type = type,
                name = name,
                icon = icon,
                color = color,
                priority = priority,
                ordinal = ordinal
            )
        }
}
