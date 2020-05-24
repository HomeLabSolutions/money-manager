package com.d9tilov.moneymanager.base.ui.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(context: Context, @DimenRes private val spaceOffset: Int) :
    RecyclerView.ItemDecoration() {
    private val space: Int = context.resources.getDimensionPixelSize(spaceOffset)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter
        if (space == 0 || adapter == null) return

        val position = parent.getChildAdapterPosition(view)
        val isFirstItem = position == 0
        val isLastItem = position == adapter.itemCount - 1

        val start: Int
        val end: Int
        when {
            isFirstItem -> {
                start = 0
                end = (space / 2f + 0.5f).toInt()
            }
            isLastItem -> {
                start = (space / 2f + 0.5f).toInt()
                end = 0
            }
            else -> {
                start = (space / 2f + 0.5f).toInt()
                end = (space / 2f + 0.5f).toInt()
            }
        }

        outRect.set(start, 0, end, 0)
    }
}
