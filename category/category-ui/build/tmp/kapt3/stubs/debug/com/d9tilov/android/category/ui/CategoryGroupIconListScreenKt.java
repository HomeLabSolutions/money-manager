package com.d9tilov.android.category.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a4\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001a2\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a3\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2!\u0010\u0010\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\b\u0010\u0013\u001a\u00020\u0001H\u0007\u00a8\u0006\u0014"}, d2 = {"CategoryGroupIconListRoute", "", "viewModel", "Lcom/d9tilov/android/category/ui/vm/CategoryGroupIconListViewModel;", "onItemClick", "Lkotlin/Function1;", "", "clickBack", "Lkotlin/Function0;", "CategoryGroupIconListScreen", "state", "Lcom/d9tilov/android/category/ui/vm/CategoryIconListUiState;", "onBackClicked", "CategoryIconListItem", "group", "Lcom/d9tilov/android/category/domain/model/CategoryGroup;", "clickCallback", "Lkotlin/ParameterName;", "name", "DefaultCategoryIconListPreview", "category-ui_debug"})
public final class CategoryGroupIconListScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void CategoryGroupIconListRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryGroupIconListViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable
    public static final void CategoryGroupIconListScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryIconListUiState state, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onItemClick) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void CategoryIconListItem(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.CategoryGroup group, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> clickCallback) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultCategoryIconListPreview() {
    }
}