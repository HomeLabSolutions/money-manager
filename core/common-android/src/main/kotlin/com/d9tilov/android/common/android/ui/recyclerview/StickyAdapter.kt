package com.d9tilov.android.common.android.ui.recyclerview

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class StickyAdapter<T : Any, SVH : RecyclerView.ViewHolder, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(diffCallback) {
    /**
     * This method gets called by [StickyHeaderItemDecorator] to fetch
     * the position of the header item in the adapter that is used for
     * (represents) item at specified position.
     *
     * @param itemPosition int. Adapter's position of the item for which to do
     * the search of the position of the header item.
     * @return int. Position of the header for an item in the adapter or
     * [RecyclerView.NO_POSITION] (-1) if an item has no header.
     */
    abstract fun getHeaderPositionForItem(itemPosition: Int): Int

    /**
     * This method gets called by [StickyHeaderItemDecorator] to setup the header View.
     *
     * @param holder RecyclerView.ViewHolder. Holder to bind the data on.
     * @param headerPosition int. Position of the header item in the adapter.
     */
    abstract fun onBindHeaderViewHolder(holder: SVH, headerPosition: Int)

    /**
     * Called only twice when [StickyHeaderItemDecorator] needs
     * a new [RecyclerView.ViewHolder] to represent a sticky header item.
     * Those two instances will be cached and used to represent a current top sticky header
     * and the moving one.
     *
     *
     * You can either create a new View manually or inflate it from an XML layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindHeaderViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup to resolve a layout params.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .onBindHeaderViewHolder
     */
    abstract fun onCreateHeaderViewHolder(parent: ViewGroup): SVH
}
