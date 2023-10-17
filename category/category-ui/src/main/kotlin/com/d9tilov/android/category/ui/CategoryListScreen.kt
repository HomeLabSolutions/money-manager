package com.d9tilov.android.category.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.ui.vm.CategoryListViewModel
import com.d9tilov.android.category.ui.vm.CategoryUiState

@Composable
fun CategoryListRoute(
    viewModel: CategoryListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryListScreen(uiState = state)
}

@Composable
fun CategoryListScreen(uiState: CategoryUiState) {
    Scaffold(
    ) { paddingValues ->
        Text(modifier = Modifier.padding(paddingValues), text = "")
    }
}
