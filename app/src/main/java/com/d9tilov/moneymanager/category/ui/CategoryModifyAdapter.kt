package com.d9tilov.moneymanager.category.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.recyclerview.CategoryDiffUtil
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.util.events.OnItemClickListener
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryBinding
import kotlinx.android.extensions.LayoutContainer
import org.xmlpull.v1.XmlPullParserException
import timber.log.Timber

class CategoryModifyAdapter :
    RecyclerView.Adapter<CategoryModifyAdapter.ModifyCategoryViewHolder>() {

    var itemClickListener: OnItemClickListener<Category>? = null
    private var categories = emptyList<Category>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModifyCategoryViewHolder {
        val viewBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ModifyCategoryViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(categories[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ModifyCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun updateItems(newCategories: List<Category>) {
        Timber.tag(App.TAG).d("newCategories size : ${newCategories.size}")
        val diffUtilsCallback =
            CategoryDiffUtil(
                categories,
                newCategories
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        diffUtilsResult.dispatchUpdatesTo(this)
        categories = newCategories
    }

    class ModifyCategoryViewHolder(private val viewBinding: ViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root),
        LayoutContainer {

        override val containerView: View
            get() = viewBinding.root

        fun bind(category: Category) {
            when (viewBinding) {
                is ItemCategoryBinding -> {
                    viewBinding.categoryItemTitle.text = category.name
                    viewBinding.categoryItemSubtitle.text = category.parent?.name
                    if (category.children.isNotEmpty()) {
                        GlideApp
                            .with(containerView.context)
                            .load(R.drawable.ic_category_folder)
                            .apply(
                                RequestOptions().override(
                                    IMAGE_SIZE_IN_PX,
                                    IMAGE_SIZE_IN_PX
                                )
                            )
                            .into(viewBinding.categoryItemIcon)
                    } else {
                        val vectorDrawable = VectorDrawableCompat.create(
                            containerView.resources,
                            category.icon,
                            null
                        ) ?: throw XmlPullParserException("Wrong vector xml file format")
                        val drawable = DrawableCompat.wrap(vectorDrawable)
                        DrawableCompat.setTint(
                            drawable.mutate(),
                            ContextCompat.getColor(containerView.context, category.color)
                        )
                        GlideApp
                            .with(containerView.context)
                            .load(drawable)
                            .apply(
                                RequestOptions().override(
                                    IMAGE_SIZE_IN_PX,
                                    IMAGE_SIZE_IN_PX
                                )
                            )
                            .into(viewBinding.categoryItemIcon)
                    }
                }
            }
        }

        companion object {
            private const val IMAGE_SIZE_IN_PX = 136
        }
    }
}
