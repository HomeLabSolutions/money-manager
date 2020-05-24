package com.d9tilov.moneymanager.base.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.category.data.entities.Category

class CategoryDiffUtil(
    private val oldCategoryList: List<Category>,
    private val newCategoryList: List<Category>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategoryList[oldItemPosition].id == newCategoryList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldCategoryList.size
    }

    override fun getNewListSize(): Int {
        return newCategoryList.size
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return newCategoryList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategoryList[oldItemPosition].name == newCategoryList[newItemPosition].name
    }
}
