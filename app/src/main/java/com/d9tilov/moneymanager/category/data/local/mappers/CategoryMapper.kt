package com.d9tilov.moneymanager.category.data.local.mappers

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.data.local.entities.CategoryDbModel
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun toDataModel(model: CategoryDbModel, parentModel: CategoryDbModel? = null) = with(model) {
        parentModel?.let {
            Category(
                id = id,
                name = name,
                parent = toDataParentModel(parentModel),
                type = type,
                icon = icon,
                color = color
            )
        } ?: Category(
            id = id,
            name = name,
            type = type,
            icon = icon,
            color = color
        )
    }

    fun toDataParentModel(model: CategoryDbModel) =
        with(model) {
            Category(
                id = id,
                name = name,
                type = type,
                icon = icon,
                color = color
            )
        }

    fun toDbModel(category: Category) =
        with(category) {
            CategoryDbModel(
                id = id,
                parentId = parent?.id ?: NO_ID,
                type = type,
                name = name,
                icon = icon,
                color = color
            )
        }

    fun toDataModelFromPrePopulate(model: PrepopulateCategory) =
        with(model) {
            CategoryDbModel(
                id = id,
                parentId = parentId,
                type = type,
                name = name,
                icon = icon,
                color = color
            )
        }
}
