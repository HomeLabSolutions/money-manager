package com.d9tilov.moneymanager.category.ui.recycler

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.databinding.ItemColorPickerBinding

class CategoryColorAdapter(@ColorRes private var chosenColor: Int?) :
    RecyclerView.Adapter<CategoryColorAdapter.CategoryColorViewHolder>() {

    var itemClickListener: OnItemClickListener<Int>? = null
    private var selectedPosition = -1

    private val categoryColorList = listOf(
        R.color.category_white,
        R.color.category_black,
        R.color.category_red,
        R.color.category_yellow,
        R.color.category_light_green,
        R.color.category_green,
        R.color.category_light_blue,
        R.color.category_blue,
        R.color.category_violet,
        R.color.category_dark_red,
        R.color.category_brick_red,
        R.color.category_dark_orange,
        R.color.category_orange,
        R.color.category_light_yellow,
        R.color.category_mint,
        R.color.category_grass_green,
        R.color.category_green_confirm,
        R.color.category_mud_green,
        R.color.category_lollipop,
        R.color.category_navy_blue,
        R.color.category_grey_blue,
        R.color.category_sad_blue,
        R.color.category_grey,
        R.color.category_purple,
        R.color.category_flower_violet,
        R.color.category_light_violet,
        R.color.category_pink
    )

    init {
        if (chosenColor == null) {
            chosenColor = R.color.category_pink
        }

        selectedPosition = categoryColorList.indexOf(chosenColor!!)
    }

    fun getSelectedPosition() = selectedPosition

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryColorViewHolder {
        val viewBinding = ItemColorPickerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = CategoryColorViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(
                    selectedPosition,
                    UNSCALE_UPDATE
                )

                val clickedColor = categoryColorList[adapterPosition]
                itemClickListener?.onItemClick(clickedColor)
                chosenColor = clickedColor
                selectedPosition = adapterPosition
                notifyItemChanged(
                    adapterPosition,
                    SCALE_UPDATE
                )
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categoryColorList.size

    override fun onBindViewHolder(
        holder: CategoryColorViewHolder,
        position: Int
    ) {
        holder.bind(categoryColorList[position], position == selectedPosition)
    }

    override fun onBindViewHolder(
        holder: CategoryColorViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        when {
            payloads.contains(SCALE_UPDATE) -> {
                holder.scale(COLOR_SELECTED_SCALE)
            }
            payloads.contains(UNSCALE_UPDATE) -> {
                holder.scale(COLOR_DEFAULT_SCALE)
            }
            else -> {
                super.onBindViewHolder(holder, position, payloads)
            }
        }
    }

    class CategoryColorViewHolder(private val viewBinding: ItemColorPickerBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(@ColorRes color: Int, isSelected: Boolean) {
            scale(if (isSelected) COLOR_SELECTED_SCALE else COLOR_DEFAULT_SCALE)
            viewBinding.colorCircle.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, color))
        }

        fun scale(scaleValue: Float) {
            val scaleDownX = ObjectAnimator.ofFloat(viewBinding.root, View.SCALE_X, scaleValue)
            val scaleDownY = ObjectAnimator.ofFloat(viewBinding.root, View.SCALE_Y, scaleValue)
            scaleDownX.duration = 200
            scaleDownY.duration = 200
            val scaleDown = AnimatorSet()
            scaleDown.play(scaleDownX).with(scaleDownY)
            scaleDown.start()
        }
    }

    companion object {
        private const val UNSCALE_UPDATE = 101
        private const val SCALE_UPDATE = 102
        private const val COLOR_DEFAULT_SCALE = 1f
        private const val COLOR_SELECTED_SCALE = 1.5f
    }
}
