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
        R.color.category_pigment_indigo,
        R.color.category_fuchsia_pink,
        R.color.category_international_klein_blue,
        R.color.category_karry,
        R.color.category_pistachio,
        R.color.category_granny_apple,
        R.color.category_corn,
        R.color.category_trinidad,
        R.color.category_black,
        R.color.category_deep_purple_a100,
        R.color.category_light_green_a200,
        R.color.category_pink_300,
        R.color.category_pink_a200,
        R.color.category_teal_100,
        R.color.category_yellow_400,
        R.color.category_screamin_green,
        R.color.category_picton_blue
    )

    init {
        if (chosenColor == null) {
            chosenColor = R.color.category_fuchsia_pink
        }

        selectedPosition = categoryColorList.indexOf(chosenColor!!)
    }

    fun getSelectedColor() = categoryColorList[selectedPosition]

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
            val adapterPosition = viewHolder.adapterPosition
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
