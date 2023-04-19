package com.d9tilov.android.goals.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GoalsRoute(viewModel: BudgetAmountViewModel = hiltViewModel(), clickBack: () -> Unit) {
}

@Composable
fun GoalsScreen(
    uiState: Goals
) {

}
