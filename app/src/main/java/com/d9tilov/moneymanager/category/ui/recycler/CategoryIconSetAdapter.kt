package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryIconBinding

class CategoryIconSetAdapter :
    RecyclerView.Adapter<CategoryIconSetAdapter.CategoryIconViewHolder>() {

    var itemClickListener: OnItemClickListener<Int>? = null

    private val categoryIconList = listOf(
        R.drawable.ic_category_cafe to R.color.category_deep_purple_a100,
        R.drawable.ic_category_car to R.color.category_light_green_a200,
        R.drawable.ic_category_doctor to R.color.category_pink_300,
        R.drawable.ic_category_entertainment to R.color.category_pink_a200,
        R.drawable.ic_category_food to R.color.category_teal_100,
        R.drawable.ic_category_internet to R.color.category_yellow_400,
        R.drawable.ic_category_home to R.color.category_screamin_green,
        R.drawable.ic_category_travels to R.color.category_picton_blue
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
        val viewHolder = CategoryIconViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(
                    categoryIconList[adapterPosition].first,
                    adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categoryIconList.size

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        holder.bind(categoryIconList[position])
    }

    class CategoryIconViewHolder(private val viewBinding: ItemCategoryIconBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(categoryItemIcon: Pair<Int, Int>) {
            val tintDrawable =
                createTintDrawable(context, categoryItemIcon.first, categoryItemIcon.second)
            GlideApp
                .with(context)
                .load(tintDrawable)
                .into(viewBinding.categoryIcon)
        }
    }
}
