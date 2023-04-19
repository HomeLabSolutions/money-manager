package com.d9tilov.android.category.ui.recycler

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.category_ui.databinding.ItemColorPickerBinding
import com.d9tilov.android.core.events.OnItemClickListener
import com.d9tilov.android.common_android.ui.base.BaseViewHolder
import com.d9tilov.android.designsystem.color.ColorManager

class CategoryColorAdapter(@ColorRes private var chosenColor: Int?, private val itemClickListener: OnItemClickListener<Int>) :
    RecyclerView.Adapter<CategoryColorAdapter.CategoryColorViewHolder>() {

    private var selectedPosition = -1

    init {
        if (chosenColor == null) chosenColor = R.color.category_pink
        selectedPosition = ColorManager.colorList.indexOf(chosenColor!!)
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

                val clickedColor = ColorManager.colorList[adapterPosition]
                itemClickListener.onItemClick(clickedColor, adapterPosition)
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

    override fun getItemCount() = ColorManager.colorList.size

    override fun onBindViewHolder(
        holder: CategoryColorViewHolder,
        position: Int
    ) {
        holder.bind(ColorManager.colorList[position], position == selectedPosition)
    }

    override fun onBindViewHolder(
        holder: CategoryColorViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        when {
            payloads.contains(SCALE_UPDATE) -> holder.scale(COLOR_SELECTED_SCALE)
            payloads.contains(UNSCALE_UPDATE) -> holder.scale(COLOR_DEFAULT_SCALE)
            else -> super.onBindViewHolder(holder, position, payloads)
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
            scaleDownX.duration = SCALE_ANIMATION_DURATION
            scaleDownY.duration = SCALE_ANIMATION_DURATION
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
        private const val SCALE_ANIMATION_DURATION = 200L
    }
}
