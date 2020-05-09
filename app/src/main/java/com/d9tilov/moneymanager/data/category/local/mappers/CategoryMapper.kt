package com.d9tilov.moneymanager.data.category.local.mappers

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.data.base.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.data.category.entities.Category
import com.d9tilov.moneymanager.data.category.local.entities.CategoryDbModel
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun toDataModel(model: CategoryDbModel, parentModel: CategoryDbModel?) = with(model) {
        parentModel?.let {
            Category(
                id = id,
                name = name,
                parent = toDataParentModel(parentModel),
                icon = icon,
                color = color
            )
        } ?: Category(
            id = id,
            name = name,
            icon = icon,
            color = color
        )
    }

    fun toDataParentModel(model: CategoryDbModel) =
        with(model) {
            Category(
                id = id,
                name = name,
                icon = icon,
                color = color
            )
        }

    fun toDbModel(category: Category) =
        with(category) {
            CategoryDbModel(
                id = id,
                parentId = parent?.id ?: NO_ID,
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
                name = name,
                icon = icon,
                color = color
            )
        }
}
