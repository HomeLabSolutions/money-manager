package com.d9tilov.moneymanager.core.ui.recyclerview;

import android.graphics.Canvas;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDragAndDropCallback extends ItemTouchHelper.SimpleCallback {

    private final RecyclerView recyclerView;

    public ItemDragAndDropCallback(RecyclerView recyclerView) {
        // Choose drag and swipe directions
        // Up and down is chosen for dragging
        // Nothing is chosen for swiping
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        // You can reorder items here
        // Reorder items only when target is not a folder
        recyclerView.getAdapter().notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // You can react for swiping items here
        // Do nothing in your case
    }

    // An item will be dropped into this folder
    private View folder;

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {

            // Here you are notified that the drag operation began

            if (folder != null) {
                folder.setBackgroundResource(0); // Clear former folder background
                folder = null;
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {

            // Here you are notified that the last operation ended

            if (folder != null) {
                // Set folder background to a color indicating
                // that an item was dropped into it
                folder.setBackgroundColor(
                        ContextCompat.getColor(
                                recyclerView.getContext(), android.R.color.holo_green_dark
                        )
                );
                // You can remove item from the list here and add it to the folder
                // Remember to notify RecyclerView about it
                recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }
    }

    // This method gets called a lot, so don't do any expensive operations here
    @Override
    public void onChildDraw(
            Canvas c,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && isCurrentlyActive) {

            // Here you are notified that the drag operation is in progress

            if (folder != null) {
                folder.setBackgroundResource(0); // Clear former folder background
                folder = null;
            }

            float itemActualPosition = viewHolder.itemView.getTop() + dY + viewHolder.itemView.getHeight() / 2;

            // Find folder under dragged item
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);

                // Exclude dragged item from detection
                if (!child.equals(viewHolder.itemView)) {

                    // Accept folder which encloses item position
                    if (child.getTop() < itemActualPosition && itemActualPosition < child.getBottom()) {

                        folder = child;
                        // Set folder background to a color indicating
                        // that an item will be dropped into it upon release
                        folder.setBackgroundColor(
                                ContextCompat.getColor(
                                        recyclerView.getContext(), android.R.color.holo_green_light
                                )
                        );
                        break;
                    }
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
