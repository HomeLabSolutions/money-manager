package com.d9tilov.moneymanager.category.data.local.mapper

import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.data.entity.CategoryDbModel
import com.d9tilov.moneymanager.category.domain.entity.categoryMap
import com.d9tilov.moneymanager.category.domain.entity.fromCategoryRes
import com.d9tilov.moneymanager.core.constants.DataConstants.NO_ID
import com.d9tilov.moneymanager.core.util.colorMap
import com.d9tilov.moneymanager.core.util.fromColorRes
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun toDataModel(model: CategoryDbModel, parentModel: CategoryDbModel? = null) = with(model) {
        parentModel?.let {
            Category.EMPTY_EXPENSE.copy(
                id = id,
                clientId = uid,
                name = name,
                parent = toDataParentModel(parentModel),
                type = type,
                icon = categoryMap.getOrElse(iconNameOrdinal) { R.drawable.ic_category_folder },
                color = colorMap.getOrElse(colorNameOrdinal) { R.color.category_all_color },
                usageCount = usageCount
            )
        } ?: Category.EMPTY_EXPENSE.copy(
            id = id,
            clientId = uid,
            name = name,
            type = type,
            icon = categoryMap.getOrElse(iconNameOrdinal) { R.drawable.ic_category_folder },
            color = colorMap.getOrElse(colorNameOrdinal) { R.color.category_all_color },
            usageCount = usageCount
        )
    }

    fun toDataParentModel(model: CategoryDbModel): Category =
        with(model) {
            Category.EMPTY_EXPENSE.copy(
                id = id,
                clientId = uid,
                name = name,
                type = type,
                icon = categoryMap.getOrElse(iconNameOrdinal) { R.drawable.ic_category_folder },
                color = colorMap.getOrElse(colorNameOrdinal) { R.color.category_all_color },
                usageCount = usageCount
            )
        }

    fun toDbModel(category: Category) =
        with(category) {
            val iconNameOrdinal = fromCategoryRes(icon)
            val colorNameOrdinal = fromColorRes(color)
            CategoryDbModel(
                id = id,
                uid = clientId,
                parentId = parent?.id ?: NO_ID,
                type = type,
                name = name,
                iconNameOrdinal = iconNameOrdinal,
                colorNameOrdinal = colorNameOrdinal,
                usageCount = usageCount
            )
        }

    fun toDataModelFromPrePopulate(model: PrepopulateCategory, uid: String) =
        with(model) {
            val iconNameOrdinal = fromCategoryRes(icon)
            val colorNameOrdinal = fromColorRes(color)
            CategoryDbModel(
                id = id,
                uid = uid,
                parentId = parentId,
                type = type,
                name = name,
                iconNameOrdinal = iconNameOrdinal,
                colorNameOrdinal = colorNameOrdinal,
                usageCount = usageCount
            )
        }
}
