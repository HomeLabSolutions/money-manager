package com.d9tilov.android.common.android.ui.recyclerview;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u0002*\b\b\u0001\u0010\u0003*\u00020\u0004*\b\b\u0002\u0010\u0005*\u00020\u00042\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00050\u0006B\u0013\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH&J\u001d\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00028\u00012\u0006\u0010\u0010\u001a\u00020\u000bH&\u00a2\u0006\u0002\u0010\u0011J\u0015\u0010\u0012\u001a\u00028\u00012\u0006\u0010\u0013\u001a\u00020\u0014H&\u00a2\u0006\u0002\u0010\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/d9tilov/android/common/android/ui/recyclerview/StickyAdapter;", "T", "", "SVH", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "VH", "Landroidx/paging/PagingDataAdapter;", "diffCallback", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "(Landroidx/recyclerview/widget/DiffUtil$ItemCallback;)V", "getHeaderPositionForItem", "", "itemPosition", "onBindHeaderViewHolder", "", "holder", "headerPosition", "(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;I)V", "onCreateHeaderViewHolder", "parent", "Landroid/view/ViewGroup;", "(Landroid/view/ViewGroup;)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "common-android_debug"})
public abstract class StickyAdapter<T extends java.lang.Object, SVH extends androidx.recyclerview.widget.RecyclerView.ViewHolder, VH extends androidx.recyclerview.widget.RecyclerView.ViewHolder> extends androidx.paging.PagingDataAdapter<T, VH> {
    
    public StickyAdapter(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.DiffUtil.ItemCallback<T> diffCallback) {
        super(null, null);
    }
    
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
    public abstract int getHeaderPositionForItem(int itemPosition);
    
    /**
     * This method gets called by [StickyHeaderItemDecorator] to setup the header View.
     *
     * @param holder RecyclerView.ViewHolder. Holder to bind the data on.
     * @param headerPosition int. Position of the header item in the adapter.
     */
    public abstract void onBindHeaderViewHolder(@org.jetbrains.annotations.NotNull
    SVH holder, int headerPosition);
    
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
    @org.jetbrains.annotations.NotNull
    public abstract SVH onCreateHeaderViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent);
}