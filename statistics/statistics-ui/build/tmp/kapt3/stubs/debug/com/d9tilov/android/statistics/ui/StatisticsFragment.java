package com.d9tilov.android.statistics.ui;

@dagger.hilt.android.AndroidEntryPoint
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 :2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0002:\u0001:B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0018\u001a\u00020\u0000H\u0016J\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001eH\u0002J\b\u0010 \u001a\u00020\u001eH\u0002J\b\u0010!\u001a\u00020\u001eH\u0002J\b\u0010\"\u001a\u00020\u001eH\u0002J\b\u0010#\u001a\u00020\u001eH\u0002J\b\u0010$\u001a\u00020\u001eH\u0002J\b\u0010%\u001a\u00020&H\u0002J\u0012\u0010\'\u001a\u00020\u001e2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\b\u0010*\u001a\u00020\u001eH\u0016J\u001a\u0010+\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020-2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\b\u0010.\u001a\u00020\u001eH\u0002J\u001c\u0010/\u001a\u00020\u001e2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020301H\u0002J\u0016\u00104\u001a\u00020\u001e2\f\u00100\u001a\b\u0012\u0004\u0012\u00020605H\u0002J\b\u00107\u001a\u00020\u001eH\u0002J\b\u0010\t\u001a\u00020\u001eH\u0002J\b\u00108\u001a\u00020\u001eH\u0002J\b\u00109\u001a\u00020\u001eH\u0002J\u0012\u00109\u001a\u00020\u001e2\b\b\u0002\u0010\u001b\u001a\u00020\u001cH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010R\u001b\u0010\u0013\u001a\u00020\u00148TX\u0094\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\u0012\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006;"}, d2 = {"Lcom/d9tilov/android/statistics/ui/StatisticsFragment;", "Lcom/d9tilov/android/common/android/ui/base/BaseFragment;", "Lcom/d9tilov/android/statistics/ui/navigation/StatisticsNavigator;", "Lcom/d9tilov/android/statistics_ui/databinding/FragmentStatisticsBinding;", "()V", "emptyViewStub", "Lcom/d9tilov/android/statistics_ui/databinding/LayoutEmptyStatisticsPlaceholderBinding;", "hideMenuHandler", "Landroid/os/Handler;", "showMenu", "", "statisticsBarChartAdapter", "Lcom/d9tilov/android/statistics/ui/recycler/StatisticsBarChartAdapter;", "statisticsMenuAdapter", "Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter;", "getStatisticsMenuAdapter", "()Lcom/d9tilov/android/statistics/ui/recycler/StatisticsMenuAdapter;", "statisticsMenuAdapter$delegate", "Lkotlin/Lazy;", "viewModel", "Lcom/d9tilov/android/statistics/ui/vm/StatisticsViewModel;", "getViewModel", "()Lcom/d9tilov/android/statistics/ui/vm/StatisticsViewModel;", "viewModel$delegate", "getNavigator", "getViewFromPeriod", "Landroid/widget/TextView;", "period", "Lcom/d9tilov/android/statistics/data/model/StatisticsPeriod;", "hideChart", "", "hideMenu", "hideMenuWithDelay", "hideViewStub", "initLineChart", "initPieChart", "initStatisticsMenuRv", "limitRange", "Lcom/google/android/material/datepicker/CalendarConstraints$Builder;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onStop", "onViewCreated", "view", "Landroid/view/View;", "openRangeCalendar", "setLineChartData", "data", "", "Lkotlinx/datetime/LocalDateTime;", "Lcom/d9tilov/android/transaction/domain/model/TransactionLineChartModel;", "setPieChartData", "", "Lcom/d9tilov/android/transaction/domain/model/TransactionChartModel;", "showChart", "showViewStub", "updatePeriod", "Companion", "statistics-ui_debug"})
public final class StatisticsFragment extends com.d9tilov.android.common.android.ui.base.BaseFragment<com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator, com.d9tilov.android.statistics_ui.databinding.FragmentStatisticsBinding> implements com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.statistics.ui.recycler.StatisticsBarChartAdapter statisticsBarChartAdapter = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy statisticsMenuAdapter$delegate = null;
    @org.jetbrains.annotations.Nullable
    private com.d9tilov.android.statistics_ui.databinding.LayoutEmptyStatisticsPlaceholderBinding emptyViewStub;
    private boolean showMenu = false;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler hideMenuHandler = null;
    @org.jetbrains.annotations.NotNull
    private static final java.math.BigDecimal PERCENT_LIMIT_TO_SHOW_LABEL = null;
    private static final int MAX_CATEGORY_NAME_LENGTH = 10;
    private static final int ANIMATION_DURATION = 500;
    private static final long HIDE_MENU_DELAY = 5000L;
    private static final int LINE_CHART_MAX_VISIBLE_VALUE_COUNT = 20;
    private static final float LINE_CHART_LINE_LENGTH = 10.0F;
    private static final float LINE_CHART_SPACE_LENGTH = 5.0F;
    private static final float LINE_CHART_SPACE_LENGTH_AXIS = 10.0F;
    private static final float LINE_CHART_PHASE = 0.0F;
    private static final float LINE_CHART_X_AXIS_MINIMUM = 0.0F;
    private static final float LINE_CHART_X_AXIS_GRANULARITY = 1.0F;
    private static final float LINE_CHART_DATA_LABEL_TEXT_SIZE = 9.0F;
    private static final float LINE_CHART_DATA_FORM_SIZE = 15.0F;
    private static final float LINE_CHART_DATA_CIRCLE_RADIUS = 1.0F;
    private static final float PIE_CHART_LEFT_OFFSET = 10.0F;
    private static final float PIE_CHART_RIGHT_OFFSET = 10.0F;
    private static final float PIE_CHART_TOP_OFFSET = 0.0F;
    private static final float PIE_CHART_BOTTOM_OFFSET = 0.0F;
    private static final float PIE_CHART_VALUE_TEXT_SIZE = 12.0F;
    private static final float PIE_CHART_SLICE_SPACE = 3.0F;
    private static final float PIE_CHART_ICONS_X_OFFSET = 0.0F;
    private static final float PIE_CHART_ICONS_Y_OFFSET = 40.0F;
    private static final float PIE_CHART_SELECTION_SHIFT = 5.0F;
    private static final float PIE_CHART_VALUE_LINE_PART_OFFSET_PERCENTAGE = 80.0F;
    private static final float PIE_CHART_VALUE_LINE_PART1_LENGTH = 0.2F;
    private static final float PIE_CHART_VALUE_LINE_PART2_LENGTH = 0.4F;
    private static final float PIE_CHART_CENTER_TEXT_SIZE = 16.0F;
    private static final float PIE_CHART_ENTRY_LABEL_TEXT_SIZE = 15.0F;
    private static final float PIE_CHART_ROTATION_ANGEL = 0.0F;
    private static final float PIE_CHART_RADIUS = 48.0F;
    private static final float PIE_CHART_DRAG_DECELERATION_FRICTION_COEF = 0.95F;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.statistics.ui.StatisticsFragment.Companion Companion = null;
    
    public StatisticsFragment() {
        super(null, 0);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.statistics.ui.StatisticsFragment getNavigator() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    protected com.d9tilov.android.statistics.ui.vm.StatisticsViewModel getViewModel() {
        return null;
    }
    
    private final com.d9tilov.android.statistics.ui.recycler.StatisticsMenuAdapter getStatisticsMenuAdapter() {
        return null;
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onStop() {
    }
    
    private final void showChart() {
    }
    
    private final void hideChart() {
    }
    
    private final void initStatisticsMenuRv() {
    }
    
    private final void showMenu() {
    }
    
    private final void hideMenu() {
    }
    
    private final void hideMenuWithDelay() {
    }
    
    private final void openRangeCalendar() {
    }
    
    private final com.google.android.material.datepicker.CalendarConstraints.Builder limitRange() {
        return null;
    }
    
    private final void updatePeriod() {
    }
    
    private final void initPieChart() {
    }
    
    private final void setPieChartData(java.util.List<com.d9tilov.android.transaction.domain.model.TransactionChartModel> data) {
    }
    
    private final void initLineChart() {
    }
    
    private final void setLineChartData(java.util.Map<kotlinx.datetime.LocalDateTime, com.d9tilov.android.transaction.domain.model.TransactionLineChartModel> data) {
    }
    
    private final void updatePeriod(com.d9tilov.android.statistics.data.model.StatisticsPeriod period) {
    }
    
    private final android.widget.TextView getViewFromPeriod(com.d9tilov.android.statistics.data.model.StatisticsPeriod period) {
        return null;
    }
    
    private final void showViewStub() {
    }
    
    private final void hideViewStub() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0012\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/d9tilov/android/statistics/ui/StatisticsFragment$Companion;", "", "()V", "ANIMATION_DURATION", "", "HIDE_MENU_DELAY", "", "LINE_CHART_DATA_CIRCLE_RADIUS", "", "LINE_CHART_DATA_FORM_SIZE", "LINE_CHART_DATA_LABEL_TEXT_SIZE", "LINE_CHART_LINE_LENGTH", "LINE_CHART_MAX_VISIBLE_VALUE_COUNT", "LINE_CHART_PHASE", "LINE_CHART_SPACE_LENGTH", "LINE_CHART_SPACE_LENGTH_AXIS", "LINE_CHART_X_AXIS_GRANULARITY", "LINE_CHART_X_AXIS_MINIMUM", "MAX_CATEGORY_NAME_LENGTH", "PERCENT_LIMIT_TO_SHOW_LABEL", "Ljava/math/BigDecimal;", "PIE_CHART_BOTTOM_OFFSET", "PIE_CHART_CENTER_TEXT_SIZE", "PIE_CHART_DRAG_DECELERATION_FRICTION_COEF", "PIE_CHART_ENTRY_LABEL_TEXT_SIZE", "PIE_CHART_ICONS_X_OFFSET", "PIE_CHART_ICONS_Y_OFFSET", "PIE_CHART_LEFT_OFFSET", "PIE_CHART_RADIUS", "PIE_CHART_RIGHT_OFFSET", "PIE_CHART_ROTATION_ANGEL", "PIE_CHART_SELECTION_SHIFT", "PIE_CHART_SLICE_SPACE", "PIE_CHART_TOP_OFFSET", "PIE_CHART_VALUE_LINE_PART1_LENGTH", "PIE_CHART_VALUE_LINE_PART2_LENGTH", "PIE_CHART_VALUE_LINE_PART_OFFSET_PERCENTAGE", "PIE_CHART_VALUE_TEXT_SIZE", "statistics-ui_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}