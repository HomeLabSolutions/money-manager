package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.diff.CategoryDiffUtil
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemLongClickListener
import com.d9tilov.moneymanager.core.events.OnItemMoveListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemTouchHelperCallback
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryBinding
import timber.log.Timber

class CategoryModifyAdapter :
    RecyclerView.Adapter<CategoryModifyAdapter.ModifyCategoryViewHolder>(),
    ItemTouchHelperCallback {

    var itemClickListener: OnItemClickListener<Category>? = null
    var itemLongClickListener: OnItemLongClickListener<Category>? = null
    var itemRemoveClickListener: OnItemClickListener<Category>? = null
    var itemMoveListener: OnItemMoveListener<Category>? = null
    var editModeEnable = false
        private set
    private var categories = mutableListOf<Category>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModifyCategoryViewHolder {
        val viewBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder =
            ModifyCategoryViewHolder(
                viewBinding
            )
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
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (payloads.contains(PAYLOAD_EDIT_MODE_ON)) {
                holder.dispatchEditMode(true)
            } else if (payloads.contains(PAYLOAD_EDIT_MODE_OFF)) {
                holder.dispatchEditMode(false)
            }
        }
    }

    override fun getItemId(position: Int) = categories[position].id

    fun updateItems(newCategories: List<Category>) {
        Timber.tag(App.TAG).d("newCategories size : ${newCategories.size}")
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
                sortedCategories
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        diffUtilsResult.dispatchUpdatesTo(this)
        categories.clear()
        categories.addAll(sortedCategories)
    }

    fun enableEditMode(enable: Boolean) {
        editModeEnable = enable
        notifyItemRangeChanged(
            0,
            categories.size,
            if (enable) PAYLOAD_EDIT_MODE_ON else PAYLOAD_EDIT_MODE_OFF
        )
    }

    override fun onItemMoveToFolder(itemPosition: Int, folderPosition: Int) {
        val itemChildren = categories[itemPosition].children
        val folderChildren = categories[folderPosition].children
        if (itemChildren.isNotEmpty() && folderChildren.isNotEmpty()) {
            return
        }
        if (itemChildren.isEmpty() && folderChildren.isEmpty()) {
            itemMoveListener?.onItemsUnitToFolder(
                categories[itemPosition],
                itemPosition,
                categories[folderPosition],
                folderPosition
            )
        } else if (itemChildren.isEmpty()) {
            itemMoveListener?.onItemAddToFolder(
                categories[itemPosition],
                itemPosition,
                categories[folderPosition],
                folderPosition
            )
        }
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
                val tintDrawable = createTintDrawable(context, category.icon, category.color)
                GlideApp
                    .with(context)
                    .load(tintDrawable)
                    .into(categoryItemIcon)
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
    }

    companion object {
        private const val PAYLOAD_EDIT_MODE_ON = 101
        private const val PAYLOAD_EDIT_MODE_OFF = 102
    }
}
