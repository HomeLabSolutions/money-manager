package com.d9tilov.android.statistics.ui.recycler;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0005\"#$%&BK\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013H\u0016J\u0010\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0013H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0013H\u0016J\u0018\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0013H\u0016J\u0014\u0010\u001f\u001a\u00020\u00192\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00110!R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "chartMenuItemClickListener", "Lcom/d9tilov/android/core/events/OnItemClickListener;", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuChartMode;", "currencyMenuItemClickListener", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuCurrency;", "inStatisticsMenuItemClickListener", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuInStatistics;", "trTypeMenuItemClickListener", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuTransactionType;", "categoryMenuItemClickListener", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuCategoryType;", "(Lcom/d9tilov/android/core/events/OnItemClickListener;Lcom/d9tilov/android/core/events/OnItemClickListener;Lcom/d9tilov/android/core/events/OnItemClickListener;Lcom/d9tilov/android/core/events/OnItemClickListener;Lcom/d9tilov/android/core/events/OnItemClickListener;)V", "menuItems", "", "Lcom/d9tilov/android/statistics/data/model/BaseStatisticsMenuType;", "getItemCount", "", "getItemId", "", "position", "getItemViewType", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateItems", "newMenuItems", "", "CategoryTypeViewHolder", "ChartModeViewHolder", "CurrencyModeViewHolder", "InStatisticsViewHolder", "TransactionTypeViewHolder", "statistics-ui_debug"})
public final class StatisticsMenuAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.d9tilov.android.common.android.ui.base.BaseViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.events.OnItemClickListener<com.d9tilov.android.statistics.data.model.StatisticsMenuChartMode> chartMenuItemClickListener = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.events.OnItemClickListener<com.d9tilov.android.statistics.data.model.StatisticsMenuCurrency> currencyMenuItemClickListener = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.events.OnItemClickListener<com.d9tilov.android.statistics.data.model.StatisticsMenuInStatistics> inStatisticsMenuItemClickListener = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.events.OnItemClickListener<com.d9tilov.android.statistics.data.model.StatisticsMenuTransactionType> trTypeMenuItemClickListener = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.events.OnItemClickListener<com.d9tilov.android.statistics.data.model.StatisticsMenuCategoryType> categoryMenuItemClickListener = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType> menuItems;
    
    public StatisticsMenuAdapter(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.events.OnItemClickListener<? super com.d9tilov.android.statistics.data.model.StatisticsMenuChartMode> chartMenuItemClickListener, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.events.OnItemClickListener<? super com.d9tilov.android.statistics.data.model.StatisticsMenuCurrency> currencyMenuItemClickListener, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.events.OnItemClickListener<? super com.d9tilov.android.statistics.data.model.StatisticsMenuInStatistics> inStatisticsMenuItemClickListener, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.events.OnItemClickListener<? super com.d9tilov.android.statistics.data.model.StatisticsMenuTransactionType> trTypeMenuItemClickListener, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.events.OnItemClickListener<? super com.d9tilov.android.statistics.data.model.StatisticsMenuCategoryType> categoryMenuItemClickListener) {
        super();
    }
    
    public final void updateItems(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType> newMenuItems) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.common.android.ui.base.BaseViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public long getItemId(int position) {
        return 0L;
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.common.android.ui.base.BaseViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter$CategoryTypeViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;)V", "bind", "", "type", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuCategoryType;", "statistics-ui_debug"})
    public static final class CategoryTypeViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding = null;
        
        public CategoryTypeViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics.data.model.StatisticsMenuCategoryType type) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter$ChartModeViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;)V", "bind", "", "mode", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuChartMode;", "statistics-ui_debug"})
    public static final class ChartModeViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding = null;
        
        public ChartModeViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics.data.model.StatisticsMenuChartMode mode) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter$CurrencyModeViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuCurrencyBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuCurrencyBinding;)V", "bind", "", "currencyType", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuCurrency;", "statistics-ui_debug"})
    public static final class CurrencyModeViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuCurrencyBinding viewBinding = null;
        
        public CurrencyModeViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuCurrencyBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics.data.model.StatisticsMenuCurrency currencyType) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter$InStatisticsViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;)V", "bind", "", "inStatisticsType", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuInStatistics;", "statistics-ui_debug"})
    public static final class InStatisticsViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding = null;
        
        public InStatisticsViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics.data.model.StatisticsMenuInStatistics inStatisticsType) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter$TransactionTypeViewHolder;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewHolder;", "viewBinding", "Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;", "(Lcom/d9tilov/android/statistics_ui/databinding/ItemStatisticsMenuBinding;)V", "bind", "", "type", "Lcom/d9tilov/android/statistics/data/model/StatisticsMenuTransactionType;", "statistics-ui_debug"})
    public static final class TransactionTypeViewHolder extends com.d9tilov.android.common.android.ui.base.BaseViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding = null;
        
        public TransactionTypeViewHolder(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics_ui.databinding.ItemStatisticsMenuBinding viewBinding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.statistics.data.model.StatisticsMenuTransactionType type) {
        }
    }
}