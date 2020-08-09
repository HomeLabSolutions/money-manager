package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.category.data.entities.CategoryIcon
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryIconBinding

class CategoryIconSetAdapter :
    RecyclerView.Adapter<CategoryIconSetAdapter.CategoryIconViewHolder>() {

    private val categoryIconList = listOf(
        CategoryIcon(R.drawable.ic_category_cafe, R.color.category_deep_purple_a100),
        CategoryIcon(R.drawable.ic_category_car, R.color.category_light_green_a200),
        CategoryIcon(R.drawable.ic_category_doctor, R.color.category_pink_300),
        CategoryIcon(R.drawable.ic_category_entertainment, R.color.category_pink_a200),
        CategoryIcon(R.drawable.ic_category_food, R.color.category_teal_100),
        CategoryIcon(R.drawable.ic_category_internet, R.color.category_yellow_400),
        CategoryIcon(R.drawable.ic_category_home, R.color.category_screamin_green),
        CategoryIcon(R.drawable.ic_category_travels, R.color.category_picton_blue)
    )

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        val viewBinding = ItemCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryIconViewHolder(viewBinding)
    }

    override fun getItemCount() = categoryIconList.size

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        holder.bind(categoryIconList[position])
    }

    class CategoryIconViewHolder(private val viewBinding: ItemCategoryIconBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(categoryItemIcon: CategoryIcon) {
            val tintDrawable =
                createTintDrawable(context, categoryItemIcon.drawableId, categoryItemIcon.colorId)
            GlideApp
                .with(context)
                .load(tintDrawable)
                .into(viewBinding.categoryIcon)
        }
    }
}
