package com.d9tilov.moneymanager.category.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.category.data.entity.Category

class CategoryDiffUtil(
    private val oldCategoryList: List<Category>,
    private val newCategoryList: List<Category>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCategoryList[oldItemPosition].id == newCategoryList[newItemPosition].id

    override fun getOldListSize() = oldCategoryList.size
    override fun getNewListSize() = newCategoryList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCategoryList[oldItemPosition] == newCategoryList[newItemPosition]
}
