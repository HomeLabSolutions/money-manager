package com.d9tilov.moneymanager.category.ui

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.diff.CategoryDiffUtil
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemLongClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwapListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemTouchHelperAdapter
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryBinding
import org.xmlpull.v1.XmlPullParserException
import timber.log.Timber
import java.util.Collections

class CategoryModifyAdapter :
    RecyclerView.Adapter<CategoryModifyAdapter.ModifyCategoryViewHolder>(),
    ItemTouchHelperAdapter {

    var itemClickListener: OnItemClickListener<Category>? = null
    var itemLongClickListener: OnItemLongClickListener<Category>? = null
    var itemRemoveClickListener: OnItemClickListener<Category>? = null
    var itemSwapListener: OnItemSwapListener<Category>? = null
    var editModeEnable = false
        private set
    private var categories = mutableListOf<Category>()

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
        viewBinding.root.setOnLongClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION && !editModeEnable) {
                itemLongClickListener?.onItemLongClick(categories[adapterPosition])
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }
        viewBinding.categoryItemRemove.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemRemoveClickListener?.onItemClick(categories[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ModifyCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun onBindViewHolder(
        holder: ModifyCategoryViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        if (payloads.contains(PAYLOAD_EDIT_MODE_ON)) {
            holder.dispatchEditMode(true)
        } else if (payloads.contains(PAYLOAD_EDIT_MODE_OFF)) {
            holder.dispatchEditMode(false)
        }
    }

    override fun getItemId(position: Int) = categories[position].id

    fun updateItems(newCategories: List<Category>) {
        Timber.tag(App.TAG).d("newCategories size : ${newCategories.size}")
        val sortedCategories = newCategories.sortedBy { it.ordinal }
        val diffUtilsCallback =
            CategoryDiffUtil(
                categories,
                sortedCategories
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, true)
        categories.clear()
        categories.addAll(sortedCategories)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun enableEditMode(enable: Boolean) {
        editModeEnable = enable
        notifyItemRangeChanged(
            0,
            categories.size,
            if (enable) PAYLOAD_EDIT_MODE_ON else PAYLOAD_EDIT_MODE_OFF
        )
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(categories, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(categories, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        itemSwapListener?.onItemSwap(categories)
        return true
    }

    override fun onItemDismiss(position: Int) {
        TODO("Not yet implemented")
    }

    class ModifyCategoryViewHolder(private val viewBinding: ItemCategoryBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(category: Category) {
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
                if (category.children.isNotEmpty()) {
                    GlideApp
                        .with(context)
                        .load(R.drawable.ic_category_folder)
                        .apply(
                            RequestOptions().override(
                                IMAGE_SIZE_IN_PX,
                                IMAGE_SIZE_IN_PX
                            )
                        )
                        .into(categoryItemIcon)
                } else {
                    val vectorDrawable = VectorDrawableCompat.create(
                        context.resources,
                        category.icon,
                        null
                    ) ?: throw XmlPullParserException("Wrong vector xml file format")
                    val drawable = DrawableCompat.wrap(vectorDrawable)
                    DrawableCompat.setTint(
                        drawable.mutate(),
                        ContextCompat.getColor(context, category.color)
                    )
                    GlideApp
                        .with(context)
                        .load(drawable)
                        .apply(
                            RequestOptions().override(
                                IMAGE_SIZE_IN_PX,
                                IMAGE_SIZE_IN_PX
                            )
                        )
                        .into(categoryItemIcon)
                }
            }
        }

        fun dispatchEditMode(enable: Boolean) {
            viewBinding.categoryItemRemove.visibility = if (enable) VISIBLE else GONE
            val animation = AnimationUtils.loadAnimation(
                context,
                R.anim.animation_shake
            )
            if (enable) {
                viewBinding.root.startAnimation(animation)
            } else {
                viewBinding.root.clearAnimation()
            }
        }

        companion object {
            private const val IMAGE_SIZE_IN_PX = 136
        }
    }

    companion object {
        private const val PAYLOAD_EDIT_MODE_ON = 101
        private const val PAYLOAD_EDIT_MODE_OFF = 102
    }
}
