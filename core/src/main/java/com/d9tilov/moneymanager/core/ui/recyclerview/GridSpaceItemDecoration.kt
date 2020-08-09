package com.d9tilov.moneymanager.core.ui.recyclerview

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridSpaceItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    @LinearLayoutCompat.OrientationMode private val orientation: Int = LinearLayoutCompat.VERTICAL
) :
    ItemDecoration() {

    private var maxColumnNumber = 0
    private var maxLineNumber = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (orientation == LinearLayoutCompat.VERTICAL) {
            if (position >= 0) {
                val line = position / spanCount
                if (line > maxLineNumber) {
                    maxLineNumber = line
                }
                val column = position % spanCount
                if (column == 0) {
                    outRect.left = spacing
                } else {
                    outRect.left = spacing / 2
                }
                val lastColumnNumber = spanCount - 1
                if (column < lastColumnNumber) {
                    outRect.right = spacing / 2
                } else {
                    outRect.right = spacing
                }
                if (line == 0) {
                    outRect.top = spacing
                } else {
                    outRect.top = spacing / 2
                }
                outRect.bottom = spacing / 2
                Log.d("moggot", "pos: $position " + " rect  = " + outRect)
            } else {
                outRect.left = 0
                outRect.right = 0
                outRect.top = 0
                outRect.bottom = 0
            }
        } else {
            if (position >= 0) {
                val line = position % spanCount
                val column = position / spanCount
                if (column > maxColumnNumber) {
                    maxColumnNumber = column
                }
                if (column > 0) {
                    outRect.left = spacing / 2
                } else {
                    outRect.left = spacing
                }
                outRect.right = spacing / 2

                if (line == 0) {
                    outRect.top = spacing
                } else {
                    outRect.top = spacing / 2
                }
                val lastLineNumber = spanCount - 1
                if (line == lastLineNumber) {
                    outRect.bottom = spacing
                } else {
                    outRect.bottom = spacing / 2
                }
            } else {
                outRect.left = 0
                outRect.right = 0
                outRect.top = 0
                outRect.bottom = 0
            }
        }
    }
}
