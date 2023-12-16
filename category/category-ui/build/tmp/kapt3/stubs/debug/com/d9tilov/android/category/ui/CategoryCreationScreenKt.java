package com.d9tilov.android.category.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\u001aD\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001a\\\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001aC\u0010\u0014\u001a\u00020\u00012\b\b\u0002\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a\b\u0010\u001e\u001a\u00020\u0001H\u0007\u001a\u000e\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006\""}, d2 = {"CategoryCreationRoute", "", "sharedViewModel", "Lcom/d9tilov/android/category/ui/vm/CategorySharedViewModel;", "viewModel", "Lcom/d9tilov/android/category/ui/vm/CategoryCreationViewModel;", "openCategoryGroupIconList", "Lkotlin/Function0;", "openCategoryIconGrid", "clickBack", "CategoryCreationScreen", "uiState", "Lcom/d9tilov/android/category/ui/vm/CategoryCreationUiState;", "onCategoryUpdated", "Lkotlin/Function1;", "Lcom/d9tilov/android/category/domain/model/Category;", "clickOnCategoryIcon", "onHideError", "onBackClicked", "onSaveClicked", "ColorListSelectorItem", "size", "Landroidx/compose/ui/unit/Dp;", "color", "", "selected", "", "onClick", "ColorListSelectorItem-kHDZbjc", "(FIZLkotlin/jvm/functions/Function1;)V", "DefaultCategoryCreationPreview", "isValid", "str", "", "category-ui_debug"})
public final class CategoryCreationScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void CategoryCreationRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategorySharedViewModel sharedViewModel, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryCreationViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> openCategoryGroupIconList, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> openCategoryIconGrid, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable
    public static final void CategoryCreationScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryCreationUiState uiState, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.category.domain.model.Category, kotlin.Unit> onCategoryUpdated, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickOnCategoryIcon, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onHideError, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onSaveClicked) {
    }
    
    public static final boolean isValid(@org.jetbrains.annotations.NotNull
    java.lang.String str) {
        return false;
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultCategoryCreationPreview() {
    }
}