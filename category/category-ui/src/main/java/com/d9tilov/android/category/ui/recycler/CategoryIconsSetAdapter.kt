package com.d9tilov.android.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.android.category_ui.databinding.ItemCategoryIconBinding
import com.d9tilov.android.core.events.OnItemClickListener
import com.d9tilov.android.common_android.ui.base.BaseViewHolder
import com.d9tilov.android.common_android.utils.createTintDrawable
import com.d9tilov.android.category.data.impl.color.ColorManager

class CategoryIconsSetAdapter(
    icons: List<Int>,
    private val itemClickListener: OnItemClickListener<Int>
) : RecyclerView.Adapter<CategoryIconsSetAdapter.CategoryIconViewHolder>() {

    private val categoryColorMap = mutableListOf<Pair<Int, Int>>()

    init {
        setHasStableIds(true)
        icons.forEachIndexed { index, icon ->
            categoryColorMap.add(icon to ColorManager.colorList[index % ColorManager.colorList.size])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        val viewBinding = ItemCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = CategoryIconViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(
                    categoryColorMap[adapterPosition].first,
                    adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categoryColorMap.size

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        holder.bind(categoryColorMap[position])
    }

    class CategoryIconViewHolder(private val viewBinding: ItemCategoryIconBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(categoryItemIcon: Pair<Int, Int>) {
            val tintDrawable =
                createTintDrawable(context, categoryItemIcon.first, categoryItemIcon.second)
            Glide
                .with(context)
                .load(tintDrawable)
                .apply(RequestOptions().override(ICON_SIZE, ICON_SIZE))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewBinding.categoryItemIcon)
        }
    }

    override fun getItemId(position: Int): Long = categoryColorMap[position].first.toLong()

    companion object {
        private const val ICON_SIZE = 120
    }
}
