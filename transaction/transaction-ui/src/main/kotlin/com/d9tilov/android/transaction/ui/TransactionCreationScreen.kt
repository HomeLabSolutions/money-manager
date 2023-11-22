package com.d9tilov.android.transaction.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel
import com.d9tilov.android.transaction.ui.vm.TransactionUiState

@Composable
fun TransactionCreationRoute(
    viewModel: TransactionCreationViewModel = hiltViewModel()
) {

    val state: TransactionUiState by viewModel.uiState.collectAsStateWithLifecycle()

}