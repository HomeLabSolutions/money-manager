package com.d9tilov.moneymanager.category.ui.recycler

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemTouchHelperCallback

class SimpleItemTouchHelperCallback(
    private val recyclerView: RecyclerView,
    private val callback: ItemTouchHelperCallback
) : ItemTouchHelper.Callback() {

    private var folder: View? = null
    private var folderPosition = -1
    private var draggedItemPosition = -1

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder
    ): Int {
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        Log.d("moggot", "onMove: ")
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && isCurrentlyActive) {

            // Here you are notified that the drag operation is in progress
            clearBackground()
            val itemActualYPosition =
                viewHolder.itemView.top + dY + viewHolder.itemView.height / 2

            val itemActualXPosition =
                viewHolder.itemView.left + dX + viewHolder.itemView.width / 2

            // Find folder under dragged item
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)

                // Exclude dragged item from detection
                if (child != viewHolder.itemView) {

                    // Accept folder which encloses item position
                    if (child.top < itemActualYPosition &&
                        itemActualYPosition < child.bottom &&
                        child.left < itemActualXPosition &&
                        child.right > itemActualXPosition
                    ) {
                        folder = child
                        // Set folder background to a color indicating
                        // that an item will be dropped into it upon release
                        folderPosition = recyclerView.getChildAdapterPosition(child)
                        folder?.background =
                            (ContextCompat.getDrawable(recyclerView.context, R.drawable.round_view))
                        break
                    }
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (viewHolder != null) {
            draggedItemPosition = viewHolder.adapterPosition
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {

            // Here you are notified that the drag operation began
            clearBackground()
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {

            // Here you are notified that the last operation ended
            if (folder != null) {
                clearBackground()
                callback.onItemMoveToFolder(draggedItemPosition, folderPosition)
            }
        }
    }

    private fun clearBackground() {
        if (folder != null) {
            folder?.setBackgroundResource(0) // Clear former folder background
            folder = null
        }
    }
}
