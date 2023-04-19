package com.d9tilov.android.category.data.impl.mapper

import com.d9tilov.android.category.data.impl.categoryMap
import com.d9tilov.android.category.data.impl.fromCategoryRes
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.category.data.model.DefaultCategory
import com.d9tilov.android.category_data_impl.R
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.toType
import com.d9tilov.android.database.entity.CategoryDbModel
import com.d9tilov.android.designsystem.color.colorMap
import com.d9tilov.android.designsystem.color.fromColorRes

fun CategoryDbModel.toDataModel(parentModel: CategoryDbModel? = null) = with(this) {
    parentModel?.let {
        Category.EMPTY_EXPENSE.copy(
            id = id,
            clientId = uid,
            name = name,
            parent = parentModel.toDataParentModel(),
            type = type.toType(),
            icon = categoryMap.getOrElse(iconNameOrdinal) { R.drawable.ic_category_folder },
            color = colorMap.getOrElse(colorNameOrdinal) { R.color.category_all_color },
            usageCount = usageCount
        )
    } ?: Category.EMPTY_EXPENSE.copy(
        id = id,
        clientId = uid,
        name = name,
        type = type.toType(),
        icon = categoryMap.getOrElse(iconNameOrdinal) { R.drawable.ic_category_folder },
        color = colorMap.getOrElse(colorNameOrdinal) { R.color.category_all_color },
        usageCount = usageCount
    )
}

fun CategoryDbModel.toDataParentModel(): Category =
    with(this) {
        Category.EMPTY_EXPENSE.copy(
            id = id,
            clientId = uid,
            name = name,
            type = type.toType(),
            icon = categoryMap.getOrElse(iconNameOrdinal) { R.drawable.ic_category_folder },
            color = colorMap.getOrElse(colorNameOrdinal) { R.color.category_all_color },
            usageCount = usageCount
        )
    }

fun Category.toDbModel() =
    with(this) {
        val iconNameOrdinal = fromCategoryRes(icon)
        val colorNameOrdinal = fromColorRes(color)
        CategoryDbModel(
            id = id,
            uid = clientId,
            parentId = parent?.id ?: NO_ID,
            type = type.value,
            name = name,
            iconNameOrdinal = iconNameOrdinal,
            colorNameOrdinal = colorNameOrdinal,
            usageCount = usageCount
        )
    }

fun DefaultCategory.toDataModelFromPrePopulate(uid: String) =
    with(this) {
        val iconNameOrdinal = fromCategoryRes(icon)
        val colorNameOrdinal = fromColorRes(color)
        CategoryDbModel(
            id = id,
            uid = uid,
            parentId = parentId,
            type = type.value,
            name = name,
            iconNameOrdinal = iconNameOrdinal,
            colorNameOrdinal = colorNameOrdinal,
            usageCount = usageCount
        )
    }
