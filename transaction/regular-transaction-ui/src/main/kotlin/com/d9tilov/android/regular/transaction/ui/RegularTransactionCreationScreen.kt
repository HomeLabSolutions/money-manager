package com.d9tilov.android.regular.transaction.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState
import com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationViewModel

@Composable
fun RegularTransactionCreationRoute(
    viewModel: RegularTransactionCreationViewModel = hiltViewModel(),
    clickBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RegularTransactionCreationScreen(
        uiState = state,
        onBackClicked = clickBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTransactionCreationScreen(
    uiState: RegularTransactionCreationUiState,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            MmTopAppBar(
                titleRes = when (uiState.transaction.type) {
                    TransactionType.EXPENSE -> com.d9tilov.android.regular_transaction_ui.R.string.regular_transaction_expense_title
                    TransactionType.INCOME -> com.d9tilov.android.regular_transaction_ui.R.string.regular_transaction_income_title
                },
                onNavigationClick = onBackClicked
            )
        }) { padding ->
        Column(modifier = Modifier.padding(top = padding.calculateTopPadding())) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultRegularTransactionCreationPreview() {
    RegularTransactionCreationScreen(
        uiState = RegularTransactionCreationUiState.EMPTY,
        onBackClicked = {}
    )
}
