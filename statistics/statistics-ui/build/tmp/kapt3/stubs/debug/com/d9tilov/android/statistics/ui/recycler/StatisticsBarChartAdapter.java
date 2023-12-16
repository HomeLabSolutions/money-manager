package com.d9tilov.android.statistics.ui.recycler;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0018B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\u0006J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\nH\u0016J\u0018\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\nH\u0016J\u0014\u0010\u0015\u001a\u00020\u000f2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u0017R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsBarChartAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/d9tilov/android/statistics/ui/recycler/StatisticsBarChartAdapter$StatisticsBarChartViewHolder;", "transactionClickListener", "Lcom/d9tilov/android/core/events/OnItemClickListener;", "Lcom/d9tilov/android/transaction/domain/model/TransactionChartModel;", "(Lcom/d9tilov/android/core/events/OnItemClickListener;)V", "data", "", "getItemCount", "", "getItemId", "", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateItems", "newData", "", "StatisticsBarChartViewHolder", "statistics-ui_debug"})
public final class StatisticsBarChartAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.d9tilov.android.statistics.ui.recycler.StatisticsBarChartAdapter.StatisticsBarChartViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.events.OnItemClickListener<com.d9tilov.android.transaction.domain.model.TransactionChartModel> transactionClickListener = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.d9tilov.android.transaction.domain.model.TransactionChartModel> data;
    
    public StatisticsBarChartAdapter(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.events.OnItemClickListener<? super com.d9tilov.android.transaction.domain.model.TransactionChartModel> transactionClickListener) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.statistics.ui.recycler.StatisticsBarChartAdapter.StatisticsBarChartViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.statistics.ui.recycler.StatisticsBarChartAdapter.StatisticsBarChartViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override
    public long getItemId(int position) {
        return 0L;
    }
    
    public final void updateItems(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.transaction.domain.model.TransactionChartModel> newData) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsBarChartAdapter$StatisticsBarChartViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsBarChartBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsBarChartBinding;)V", "bind", "", "item", "Lcom/d9tilov/android/transaction/domain/model/TransactionChartModel;", "Companion", "statistics-ui_debug"})
    public static final class StatisticsBarChartViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemStatisticsBarChartBinding viewBinding = null;
        private static final int MAX_PERCENT_AMOUNT = 100;
        private static final double STATISTICAL_ERROR = 0.01;
        @org.jetbrains.annotations.NotNull
        public static final com.d9tilov.android.statistics.ui.recycler.StatisticsBarChartAdapter.StatisticsBarChartViewHolder.Companion Companion = null;
        
        public StatisticsBarChartViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemStatisticsBarChartBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.transaction.domain.model.TransactionChartModel item) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsBarChartAdapter$StatisticsBarChartViewHolder$Companion;", "", "()V", "MAX_PERCENT_AMOUNT", "", "STATISTICAL_ERROR", "", "statistics-ui_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
        }
    }
}