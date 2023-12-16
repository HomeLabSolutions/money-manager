package com.d9tilov.android.statistics.ui;

@dagger.hilt.android.AndroidEntryPoint
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000f\u001a\u00020\u0000H\u0016J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0007\u001a\u0004\u0018\u00010\u0012H\u0002J\u001a\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8TX\u0094\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0018"}, d2 = {"Lcom/d9tilov/android/statistics/ui/FragmentStatisticsDetails;", "Lcom/d9tilov/android/common/android/ui/base/BaseFragment;", "Lcom/d9tilov/android/statistics/ui/navigation/StatisticsDetailsNavigator;", "Lcom/d9tilov/android/statistics_ui/databinding/FragmentStatisticsDetailsBinding;", "()V", "adapter", "Lcom/d9tilov/android/statistics/ui/recycler/StatisticsDetailsAdapter;", "toolbar", "Lcom/google/android/material/appbar/MaterialToolbar;", "viewModel", "Lcom/d9tilov/android/statistics/ui/vm/StatisticsDetailsViewModel;", "getViewModel", "()Lcom/d9tilov/android/statistics/ui/vm/StatisticsDetailsViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "getNavigator", "initToolbar", "", "Landroidx/appcompat/widget/Toolbar;", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "statistics-ui_debug"})
public final class FragmentStatisticsDetails extends com.d9tilov.android.common.android.ui.base.BaseFragment<com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator, com.d9tilov.android.statistics_ui.databinding.FragmentStatisticsDetailsBinding> implements com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.statistics.ui.recycler.StatisticsDetailsAdapter adapter = null;
    @org.jetbrains.annotations.Nullable
    private com.google.android.material.appbar.MaterialToolbar toolbar;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    
    public FragmentStatisticsDetails() {
        super(null, 0);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.statistics.ui.FragmentStatisticsDetails getNavigator() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    protected com.d9tilov.android.statistics.ui.vm.StatisticsDetailsViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initToolbar(androidx.appcompat.widget.Toolbar toolbar) {
    }
}