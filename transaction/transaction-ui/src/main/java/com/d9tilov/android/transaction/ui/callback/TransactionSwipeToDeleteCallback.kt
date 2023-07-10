package com.d9tilov.android.transaction.ui.callback

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.android.common_android.utils.px
import com.d9tilov.android.common_android.utils.getColorFromAttr
import com.d9tilov.android.common_android.utils.setTextAppearanceFromAttr
import com.d9tilov.android.transaction.domain.model.BaseTransaction
import com.d9tilov.android.transaction_ui.R
import java.util.Locale

abstract class TransactionSwipeToDeleteCallback(val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, com.d9tilov.android.common_android.R.drawable.ic_delete_swipe)
    private var intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
    private var intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
    private val background = ColorDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private val textPaint = Paint()
    private val textView = TextView(context)
    private val boundRect = Rect()
    private val text = context.getString(R.string.delete).uppercase(Locale.getDefault())

    companion object {
        private val TEXT_RIGHT_MARGIN = 12.px
        private val MARGIN_BETWEEN_TEXT_AND_ICON = 8.px
    }

    init {
        context.setTextAppearanceFromAttr(textView, com.google.android.material.R.attr.textAppearanceLabelLarge)
        textPaint.color = context.getColorFromAttr(com.google.android.material.R.attr.colorOnPrimary)
        textPaint.textSize = textView.textSize
        textPaint.typeface = textView.typeface
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder.itemViewType == BaseTransaction.HEADER) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        val backgroundColor = context.getColorFromAttr(androidx.appcompat.R.attr.colorError)
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        textPaint.getTextBounds(text, 0, text.length, boundRect)
        val textHeight = boundRect.height()
        val textWidth = boundRect.width()

        val textLeft = itemView.right - TEXT_RIGHT_MARGIN - textWidth
        itemView.right - TEXT_RIGHT_MARGIN
        val textTop = itemView.top + ((itemHeight + textHeight) / 2).toFloat()

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft =
            itemView.right - TEXT_RIGHT_MARGIN - textWidth - MARGIN_BETWEEN_TEXT_AND_ICON - intrinsicWidth
        val deleteIconRight =
            itemView.right - TEXT_RIGHT_MARGIN - textWidth - MARGIN_BETWEEN_TEXT_AND_ICON
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        deleteIcon?.setBounds(
            deleteIconLeft.toInt(),
            deleteIconTop,
            deleteIconRight.toInt(),
            deleteIconBottom
        )
        if (dX < -(TEXT_RIGHT_MARGIN + textWidth + MARGIN_BETWEEN_TEXT_AND_ICON + intrinsicWidth)) {
            deleteIcon?.draw(c)
            c.drawText(
                text,
                textLeft,
                textTop,
                textPaint
            )
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}
