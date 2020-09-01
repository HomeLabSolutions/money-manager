package com.d9tilov.moneymanager.category.data.local.mapper

import android.content.Context
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.data.local.entity.CategoryDbModel
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoryMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun toDataModel(model: CategoryDbModel, parentModel: CategoryDbModel? = null) = with(model) {
        val iconResId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
        val colorResId = context.resources.getIdentifier(colorName, "color", context.packageName)
        parentModel?.let {
            Category(
                id = id,
                clientId = uid,
                name = name,
                parent = toDataParentModel(parentModel),
                type = type,
                icon = iconResId,
                color = colorResId,
                usageCount = usageCount
            )
        } ?: Category(
            id = id,
            clientId = uid,
            name = name,
            type = type,
            icon = iconResId,
            color = colorResId,
            usageCount = usageCount
        )
    }

    fun toDataParentModel(model: CategoryDbModel) =
        with(model) {
            val iconResId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
            val colorResId = context.resources.getIdentifier(colorName, "color", context.packageName)
            Category(
                id = id,
                clientId = uid,
                name = name,
                type = type,
                icon = iconResId,
                color = colorResId,
                usageCount = usageCount
            )
        }

    fun toDbModel(category: Category) =
        with(category) {
            val iconName = context.resources.getResourceEntryName(icon)
            val colorName = context.resources.getResourceEntryName(color)
            CategoryDbModel(
                id = id,
                uid = clientId,
                parentId = parent?.id ?: NO_ID,
                type = type,
                name = name,
                iconName = iconName,
                colorName = colorName,
                usageCount = usageCount
            )
        }

    fun toDataModelFromPrePopulate(model: PrepopulateCategory, uid: String) =
        with(model) {
            val iconName = context.resources.getResourceEntryName(icon)
            val colorName = context.resources.getResourceEntryName(color)
            CategoryDbModel(
                id = id,
                uid = uid,
                parentId = parentId,
                type = type,
                name = name,
                iconName = iconName,
                colorName = colorName,
                usageCount = usageCount
            )
        }
}
