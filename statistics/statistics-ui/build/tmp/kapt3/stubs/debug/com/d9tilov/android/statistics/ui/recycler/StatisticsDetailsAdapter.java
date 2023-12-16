package com.d9tilov.android.statistics.ui.recycler;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0016J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\bH\u0016J\u0018\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bH\u0016J\u0014\u0010\u0013\u001a\u00020\r2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u0015R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsDetailsAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/d9tilov/android/statistics/ui/recycler/StatisticsDetailsAdapter$TransactionDetailsViewHolder;", "()V", "transactions", "", "Lcom/d9tilov/android/transaction/domain/model/Transaction;", "getItemCount", "", "getItemId", "", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateItems", "newData", "", "TransactionDetailsViewHolder", "statistics-ui_debug"})
public final class StatisticsDetailsAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.d9tilov.android.statistics.ui.recycler.StatisticsDetailsAdapter.TransactionDetailsViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.d9tilov.android.transaction.domain.model.Transaction> transactions = null;
    
    public StatisticsDetailsAdapter() {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.statistics.ui.recycler.StatisticsDetailsAdapter.TransactionDetailsViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.statistics.ui.recycler.StatisticsDetailsAdapter.TransactionDetailsViewHolder holder, int position) {
    }
    
    public final void updateItems(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.transaction.domain.model.Transaction> newData) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override
    public long getItemId(int position) {
        return 0L;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsDetailsAdapter$TransactionDetailsViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemTransactionDetailsBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemTransactionDetailsBinding;)V", "bind", "", "transaction", "Lcom/d9tilov/android/transaction/domain/model/Transaction;", "Companion", "statistics-ui_debug"})
    public static final class TransactionDetailsViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemTransactionDetailsBinding viewBinding = null;
        private static final int IMAGE_SIZE_IN_PX = 136;
        @org.jetbrains.annotations.NotNull
        public static final com.d9tilov.android.statistics.ui.recycler.StatisticsDetailsAdapter.TransactionDetailsViewHolder.Companion Companion = null;
        
        public TransactionDetailsViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemTransactionDetailsBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.transaction.domain.model.Transaction transaction) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsDetailsAdapter$TransactionDetailsViewHolder$Companion;", "", "()V", "IMAGE_SIZE_IN_PX", "", "statistics-ui_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
        }
    }
}