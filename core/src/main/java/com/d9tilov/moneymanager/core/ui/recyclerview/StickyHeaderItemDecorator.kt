package com.d9tilov.moneymanager.core.ui.recyclerview

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

class StickyHeaderItemDecorator<T : Any>(private val adapter: StickyAdapter<T, RecyclerView.ViewHolder, RecyclerView.ViewHolder>) :
    ItemDecoration() {
    private var currentStickyPosition = NO_POSITION
    private var recyclerView: RecyclerView? = null
    private lateinit var currentStickyHolder: RecyclerView.ViewHolder
    private var lastViewOverlappedByHeader: View? = null

    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView === recyclerView) {
            return // nothing to do
        }
        recyclerView?.let { destroyCallbacks(it) }
        this.recyclerView = recyclerView
        if (recyclerView != null) {
            currentStickyHolder = adapter.onCreateHeaderViewHolder(recyclerView)
            fixLayoutSize()
            setupCallbacks()
        }
    }

    private fun setupCallbacks() {
        recyclerView?.addItemDecoration(this)
    }

    private fun destroyCallbacks(recyclerView: RecyclerView) {
        recyclerView.removeItemDecoration(this)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val layoutManager = parent.layoutManager ?: return
        var topChildPosition = NO_POSITION
        if (layoutManager is LinearLayoutManager) {
            topChildPosition = layoutManager.findFirstVisibleItemPosition()
        } else {
            val topChild: View? = parent.getChildAt(0)
            if (topChild != null) {
                topChildPosition = parent.getChildAdapterPosition(topChild)
            }
        }
        if (topChildPosition == NO_POSITION) {
            return
        }
        var viewOverlappedByHeader: View? =
            getChildInContact(parent, currentStickyHolder.itemView.bottom)
        if (viewOverlappedByHeader == null) {
            viewOverlappedByHeader = if (lastViewOverlappedByHeader != null) {
                lastViewOverlappedByHeader
            } else {
                parent.getChildAt(topChildPosition)
            }
        }
        lastViewOverlappedByHeader = viewOverlappedByHeader
        val overlappedByHeaderPosition =
            viewOverlappedByHeader?.let { parent.getChildAdapterPosition(viewOverlappedByHeader) }
                ?: 0
        if (overlappedByHeaderPosition == NO_POSITION) {
            return
        }
        val overlappedHeaderPosition: Int
        val preOverlappedPosition: Int
        if (overlappedByHeaderPosition > 0) {
            preOverlappedPosition = adapter.getHeaderPositionForItem(overlappedByHeaderPosition - 1)
            overlappedHeaderPosition = adapter.getHeaderPositionForItem(overlappedByHeaderPosition)
        } else {
            preOverlappedPosition = adapter.getHeaderPositionForItem(topChildPosition)
            overlappedHeaderPosition = preOverlappedPosition
        }
        if (preOverlappedPosition == NO_POSITION) {
            return
        }
        viewOverlappedByHeader?.let {
            if (preOverlappedPosition != overlappedHeaderPosition && shouldMoveHeader(it)) {
                updateStickyHeader(topChildPosition)
                moveHeader(c, it)
            } else {
                updateStickyHeader(topChildPosition)
                drawHeader(c)
            }
        }
    }

    // shouldMoveHeader returns the sticky header should move or not.
    // This method is for avoiding sinking/departing the sticky header into/from top of screen
    private fun shouldMoveHeader(viewOverlappedByHeader: View): Boolean {
        val dy: Int = viewOverlappedByHeader.top - viewOverlappedByHeader.height
        return viewOverlappedByHeader.top >= 0 && dy <= 0
    }

    private fun updateStickyHeader(topChildPosition: Int) {
        val headerPositionForItem = adapter.getHeaderPositionForItem(topChildPosition)
        if (headerPositionForItem != currentStickyPosition && headerPositionForItem != NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem)
            currentStickyPosition = headerPositionForItem
        } else if (headerPositionForItem != NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem)
        }
    }

    private fun drawHeader(c: Canvas) {
        c.save()
        c.translate(0f, 0f)
        currentStickyHolder.itemView.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - nextHeader.height.toFloat())
        currentStickyHolder.itemView.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixLayoutSize() {
        recyclerView?.run {
            viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        // Specs for parent (RecyclerView)
                        val widthSpec: Int = View.MeasureSpec.makeMeasureSpec(
                            width,
                            View.MeasureSpec.EXACTLY
                        )
                        val heightSpec: Int = View.MeasureSpec.makeMeasureSpec(
                            height,
                            View.MeasureSpec.UNSPECIFIED
                        )

                        // Specs for children (headers)
                        val childWidthSpec = ViewGroup.getChildMeasureSpec(
                            widthSpec,
                            paddingLeft + paddingRight,
                            currentStickyHolder.itemView.layoutParams.width
                        )
                        val childHeightSpec = ViewGroup.getChildMeasureSpec(
                            heightSpec,
                            paddingTop + paddingBottom,
                            currentStickyHolder.itemView.layoutParams.height
                        )
                        currentStickyHolder.itemView.measure(childWidthSpec, childHeightSpec)
                        currentStickyHolder.itemView.layout(
                            0, 0,
                            currentStickyHolder.itemView.measuredWidth,
                            currentStickyHolder.itemView.measuredHeight
                        )
                    }
                })
        }
    }
}
