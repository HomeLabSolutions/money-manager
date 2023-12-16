package com.d9tilov.android.profile.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0007\u001a\u0010\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0012\u0010\u0005\u001a\u00020\u00012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0007\u001a\u0012\u0010\b\u001a\u00020\u00012\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0007\u001af\u0010\u000b\u001a\u00020\u00012\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0007\u001a\u0096\u0001\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0007\u001a \u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020%2\u000e\b\u0002\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0007\u00a8\u0006\'"}, d2 = {"DefaultPreviewProfile", "", "ProfileCard", "userProfile", "Lcom/d9tilov/android/profile/ui/vm/UserUiProfile;", "ProfileContent", "name", "", "ProfilePicture", "uri", "Landroid/net/Uri;", "ProfileRoute", "viewModel", "Lcom/d9tilov/android/profile/ui/vm/ProfileViewModel;", "navigateToCurrencyListScreen", "Lkotlin/Function0;", "navigateToBudgetScreen", "navigateToRegularIncomeScreen", "navigateToRegularExpenseScreen", "navigateToGoalsScreen", "navigateToSettingsScreen", "ProfileScreen", "state", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiState;", "showDialog", "", "onCurrencyClicked", "onBudgetClicked", "onRegularIncomeClicked", "onRegularExpenseClicked", "onGoalsClicked", "onSettingsClicked", "onLogoutClicked", "onLogoutConfirmClicked", "onLogoutDismissClicked", "ProfileSection", "profileUiItem", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "navigationCallback", "profile-ui_debug"})
public final class ProfileScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void ProfileRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.profile.ui.vm.ProfileViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateToCurrencyListScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateToBudgetScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateToRegularIncomeScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateToRegularExpenseScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateToGoalsScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateToSettingsScreen) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ProfileScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.profile.ui.vm.ProfileUiState state, boolean showDialog, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onCurrencyClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBudgetClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onRegularIncomeClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onRegularExpenseClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onGoalsClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onSettingsClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onLogoutClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onLogoutConfirmClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onLogoutDismissClicked) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ProfileCard(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.profile.ui.vm.UserUiProfile userProfile) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ProfilePicture(@org.jetbrains.annotations.Nullable
    android.net.Uri uri) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ProfileContent(@org.jetbrains.annotations.Nullable
    java.lang.String name) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material.ExperimentalMaterialApi.class})
    @androidx.compose.runtime.Composable
    public static final void ProfileSection(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.profile.ui.vm.ProfileUiItem profileUiItem, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigationCallback) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultPreviewProfile() {
    }
}