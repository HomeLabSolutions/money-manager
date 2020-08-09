package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.App.Companion.TAG
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.diff.CategoryDiffUtil
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryBaseBinding
import com.d9tilov.moneymanager.databinding.ItemCategoryBinding
import timber.log.Timber

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var itemClickListener: OnItemClickListener<Category>? = null
    private var categories = mutableListOf<Category>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewBinding = if (viewType == ALL) ItemCategoryBaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ) else
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder =
            CategoryViewHolder(
                viewBinding
            )
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != NO_POSITION) {
                itemClickListener?.onItemClick(categories[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun updateItems(newCategories: List<Category>) {
        Timber.tag(TAG).d("newCategories size : ${newCategories.size}")
        val sortedCategories = newCategories.sortedWith(
            compareBy(
                { it.children.isEmpty() },
                { -it.usageCount },
                { it.name }
            )
        )
        val diffUtilsCallback =
            CategoryDiffUtil(
                categories,
                newCategories
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        categories.clear()
        categories.addAll(sortedCategories)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int) = if (position == 0) ALL else ORDINARY

    class CategoryViewHolder(private val viewBinding: ViewBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(category: Category) {
            when (viewBinding) {
                is ItemCategoryBinding -> {
                    viewBinding.run {
                        categoryItemTitle.text = category.name
                        categoryItemTitle.setTextColor(
                            ContextCompat.getColor(
                                context,
                                category.color
                            )
                        )
                        categoryItemSubtitle.text = category.parent?.name
                        val parentColor = ColorUtils.setAlphaComponent(
                            ContextCompat.getColor(
                                context,
                                category.color
                            ),
                            240
                        )
                        categoryItemSubtitle.setTextColor(parentColor)
                        val drawable = createTintDrawable(context, category.icon, category.color)
                        GlideApp
                            .with(context)
                            .load(drawable)
                            .into(categoryItemIcon)
                    }
                }
            }
        }
    }

    private companion object {

        private const val ALL = 0
        private const val ORDINARY = 1
    }
}
