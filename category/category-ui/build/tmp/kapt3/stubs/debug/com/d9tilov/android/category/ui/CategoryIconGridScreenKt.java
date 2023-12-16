package com.d9tilov.android.category.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0007\u001a2\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0007\u001a\b\u0010\u0011\u001a\u00020\u0001H\u0007\u00a8\u0006\u0012"}, d2 = {"CategoryIconGridRoute", "", "sharedViewModel", "Lcom/d9tilov/android/category/ui/vm/CategorySharedViewModel;", "viewModel", "Lcom/d9tilov/android/category/ui/vm/CategoryIconGridViewModel;", "onIconClick", "Lkotlin/Function1;", "", "clickBack", "Lkotlin/Function0;", "CategoryIconGridScreen", "uiState", "Lcom/d9tilov/android/category/ui/vm/CategoryIconGridUiState;", "onIconClicked", "", "onBackClicked", "DefaultCategoryIconGridPreview", "category-ui_debug"})
public final class CategoryIconGridScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void CategoryIconGridRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategorySharedViewModel sharedViewModel, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryIconGridViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onIconClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable
    public static final void CategoryIconGridScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryIconGridUiState uiState, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onIconClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultCategoryIconGridPreview() {
    }
}