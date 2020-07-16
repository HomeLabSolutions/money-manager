package com.d9tilov.moneymanager.base.ui.recyclerview.decoration;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.d9tilov.moneymanager.base.ui.recyclerview.adapter.StickyAdapter;

public class StickyHeaderItemDecorator2 extends RecyclerView.ItemDecoration {

    private static final String TAG = "moggot";
    private StickyAdapter adapter;
    private int currentStickyPosition = RecyclerView.NO_POSITION;
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder currentStickyHolder;
    private View lastViewOverlappedByHeader = null;

    public StickyHeaderItemDecorator2(@NonNull StickyAdapter adapter) {
        this.adapter = adapter;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (this.recyclerView == recyclerView) {
            return; // nothing to do
        }
        if (this.recyclerView != null) {
            destroyCallbacks(this.recyclerView);
        }
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            currentStickyHolder = adapter.onCreateHeaderViewHolder(recyclerView);
            fixLayoutSize();
            setupCallbacks();
        }
    }

    private void setupCallbacks() {
        recyclerView.addItemDecoration(this);
    }

    private void destroyCallbacks(RecyclerView recyclerView) {
        recyclerView.removeItemDecoration(this);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }

        int topChildPosition = RecyclerView.NO_POSITION;
        if (layoutManager instanceof LinearLayoutManager) {
            topChildPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else {
            View topChild = parent.getChildAt(0);
            if (topChild != null) {
                topChildPosition = parent.getChildAdapterPosition(topChild);
            }
        }

        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }

        View viewOverlappedByHeader = getChildInContact(parent, currentStickyHolder.itemView.getBottom());
        if (viewOverlappedByHeader == null) {
            if (lastViewOverlappedByHeader != null) {
                viewOverlappedByHeader = lastViewOverlappedByHeader;
            } else {
                viewOverlappedByHeader = parent.getChildAt(topChildPosition);
            }
        }
        lastViewOverlappedByHeader = viewOverlappedByHeader;

        int overlappedByHeaderPosition = parent.getChildAdapterPosition(viewOverlappedByHeader);
        int overlappedHeaderPosition;
        int preOverlappedPosition;
        if (overlappedByHeaderPosition > 0) {
            preOverlappedPosition = adapter.getHeaderPositionForItem(overlappedByHeaderPosition - 1);
            overlappedHeaderPosition = adapter.getHeaderPositionForItem(overlappedByHeaderPosition);
        } else {
            preOverlappedPosition = adapter.getHeaderPositionForItem(topChildPosition);
            overlappedHeaderPosition = preOverlappedPosition;
        }

        if (preOverlappedPosition == RecyclerView.NO_POSITION) {
            return;
        }

        if (preOverlappedPosition != overlappedHeaderPosition && shouldMoveHeader(viewOverlappedByHeader)) {
            android.util.Log.d(TAG, "shouldMoveHeader: ");
            updateStickyHeader(topChildPosition, overlappedByHeaderPosition);
            moveHeader(c, viewOverlappedByHeader);
        } else {
            updateStickyHeader(topChildPosition, RecyclerView.NO_POSITION);
            drawHeader(c);
        }
    }

    // shouldMoveHeader returns the sticky header should move or not.
    // This method is for avoiding sinking/departing the sticky header into/from top of screen
    private boolean shouldMoveHeader(View viewOverlappedByHeader) {
        int dy = (viewOverlappedByHeader.getTop() - viewOverlappedByHeader.getHeight());
        return (viewOverlappedByHeader.getTop() >= 0 && dy <= 0);
    }

    @SuppressWarnings("unchecked")
    private void updateStickyHeader(int topChildPosition, int contactChildPosition) {
        int headerPositionForItem = adapter.getHeaderPositionForItem(topChildPosition);
//        android.util.Log.d(TAG, "updateStickyHeader pos: " + topChildPosition);
        if (headerPositionForItem != currentStickyPosition && headerPositionForItem != RecyclerView.NO_POSITION) {
//            android.util.Log.d(TAG, "updateStickyHeader1");
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem);
            currentStickyPosition = headerPositionForItem;
        } else if (headerPositionForItem != RecyclerView.NO_POSITION) {
//            android.util.Log.d(TAG, "updateStickyHeader2");
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem);
        }
    }

    private void drawHeader(Canvas c) {
        android.util.Log.d(TAG, "drawHeader");
        c.save();
        c.translate(0, 0);
        currentStickyHolder.itemView.draw(c);
        c.restore();
    }

    private void moveHeader(Canvas c, View nextHeader) {
        android.util.Log.d(TAG, "moveHeader");
        c.save();
        c.translate(0, nextHeader.getTop() - nextHeader.getHeight());
        currentStickyHolder.itemView.draw(c);
        c.restore();
    }

    private View getChildInContact(RecyclerView parent, int contactPoint) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getBottom() > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    private void fixLayoutSize() {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Specs for parent (RecyclerView)
                int widthSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), View.MeasureSpec.UNSPECIFIED);

                // Specs for children (headers)
                int childWidthSpec = ViewGroup.getChildMeasureSpec(
                        widthSpec,
                        recyclerView.getPaddingLeft() + recyclerView.getPaddingRight(),
                        currentStickyHolder.itemView.getLayoutParams().width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(
                        heightSpec,
                        recyclerView.getPaddingTop() + recyclerView.getPaddingBottom(),
                        currentStickyHolder.itemView.getLayoutParams().height);

                currentStickyHolder.itemView.measure(childWidthSpec, childHeightSpec);

                currentStickyHolder.itemView.layout(0, 0,
                        currentStickyHolder.itemView.getMeasuredWidth(),
                        currentStickyHolder.itemView.getMeasuredHeight());
            }
        });
    }
}
