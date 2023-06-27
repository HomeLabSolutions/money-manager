package com.d9tilov.android.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.diff.CategoryDiffUtil
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.ItemCategoryBaseBinding
import com.d9tilov.android.category_ui.databinding.ItemCategoryBinding
import com.d9tilov.android.common_android.ui.base.BaseViewHolder
import com.d9tilov.android.common_android.utils.createTintDrawable
import com.d9tilov.android.core.events.OnItemClickListener

class CategoryAdapter(private val itemClickListener: OnItemClickListener<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories = mutableListOf<Category>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewBinding = if (viewType == ALL) ItemCategoryBaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ) else {
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        val viewHolder = CategoryViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != NO_POSITION) {
                itemClickListener.onItemClick(categories[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemId(position: Int) = categories[position].id

    fun updateItems(newCategories: List<Category>) {
        val diffUtilsCallback =
            CategoryDiffUtil(
                categories,
                newCategories
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        categories.clear()
        categories.addAll(newCategories)
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
                        category.color?.let { color ->
                            categoryItemTitle.setTextColor(ContextCompat.getColor(context, color))
                            val parentColor = ColorUtils.setAlphaComponent(
                                ContextCompat.getColor(context, color),
                                COLOR_ALPHA
                            )
                            categoryItemSubtitle.setTextColor(parentColor)
                            category.icon?.let { icon ->
                                val drawable = createTintDrawable(context, icon, color)
                                Glide
                                    .with(context)
                                    .load(drawable)
                                    .into(categoryItemIcon)
                            }
                        }
                        categoryItemSubtitle.text = category.parent?.name

                    }
                }
                is ItemCategoryBaseBinding -> {
                    viewBinding.run {
                        categoryItemTitle.text = context.getString(R.string.category_all)
                        category.color?.let { color ->
                            categoryItemTitle.setTextColor(ContextCompat.getColor(context, color))
                            val parentColor = ColorUtils.setAlphaComponent(
                                ContextCompat.getColor(context, color),
                                COLOR_ALPHA
                            )
                            categoryItemSubtitle.setTextColor(parentColor)
                            category.icon?.let { icon ->
                                val drawable = createTintDrawable(context, icon, color)
                                Glide
                                    .with(context)
                                    .load(drawable)
                                    .into(categoryItemIcon)
                            }
                        }
                        categoryItemSubtitle.text = category.parent?.name
                    }
                }
            }
        }
    }

    private companion object {
        private const val ALL = 0
        private const val ORDINARY = 1
        private const val COLOR_ALPHA = 240
    }
}
