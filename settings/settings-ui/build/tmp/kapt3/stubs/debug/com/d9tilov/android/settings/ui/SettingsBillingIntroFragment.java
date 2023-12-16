package com.d9tilov.android.settings.ui;

@dagger.hilt.android.AndroidEntryPoint
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\u0002H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u001a\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8TX\u0094\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/d9tilov/android/settings/ui/SettingsBillingIntroFragment;", "Lcom/d9tilov/android/common/android/ui/base/BaseFragment;", "Lcom/d9tilov/android/settings/ui/navigation/SettingsBillingNavigator;", "Lcom/d9tilov/android/settings_ui/databinding/FragmentBillingIntroBinding;", "()V", "toolbar", "Lcom/google/android/material/appbar/MaterialToolbar;", "viewModel", "Lcom/d9tilov/android/settings/ui/vm/SettingsBillingIntroViewModel;", "getViewModel", "()Lcom/d9tilov/android/settings/ui/vm/SettingsBillingIntroViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "getNavigator", "initToolbar", "", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "settings-ui_debug"})
public final class SettingsBillingIntroFragment extends com.d9tilov.android.common.android.ui.base.BaseFragment<com.d9tilov.android.settings.ui.navigation.SettingsBillingNavigator, com.d9tilov.android.settings_ui.databinding.FragmentBillingIntroBinding> implements com.d9tilov.android.settings.ui.navigation.SettingsBillingNavigator {
    @org.jetbrains.annotations.Nullable
    private com.google.android.material.appbar.MaterialToolbar toolbar;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    
    public SettingsBillingIntroFragment() {
        super(null, 0);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.settings.ui.navigation.SettingsBillingNavigator getNavigator() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    protected com.d9tilov.android.settings.ui.vm.SettingsBillingIntroViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initToolbar() {
    }
}