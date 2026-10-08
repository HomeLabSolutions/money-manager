package com.d9tilov.android.statistics.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsViewModel
import com.d9tilov.android.transaction.ui.TransactionItem
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import java.math.BigDecimal
import com.d9tilov.android.common.android.R as CommonAndroidR

@Composable
fun StatisticsDetailsRoute(
    viewModel: StatisticsDetailsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onTransactionClicked: (Long) -> Unit,
) {
    val uiState: StatisticsDetailsUiState by viewModel.uiState.collectAsState(StatisticsDetailsUiState())
    StatisticsDetailsScreen(
        state = uiState,
        onBackClicked = onBackClicked,
        onTransactionClicked = onTransactionClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsDetailsScreen(
    modifier: Modifier = Modifier,
    state: StatisticsDetailsUiState,
    onBackClicked: () -> Unit,
    onTransactionClicked: (Long) -> Unit,
) {
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = R.string.statistics_details_title,
            onNavigationClick = onBackClicked,
        )
    }) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = modifier.consumeWindowInsets(paddingValues),
            state = rememberLazyListState(),
        ) {
            items(items = state.transactions, key = { item -> item.id }) { item ->
                TransactionItem(
                    Modifier.fillMaxWidth().clickable { onTransactionClicked(item.id) },
                    item,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsDetailsScreenPreview() {
    MoneyManagerTheme {
        StatisticsDetailsScreen(
            state =
                StatisticsDetailsUiState(
                    categoryName = "MyCategory",
                    transactions =
                        listOf("Category1", "Category2", "Category3").mapIndexed { index, name ->
                            TransactionUiModel.EMPTY.copy(
                                id = (index + 1).toLong(),
                                category =
                                    Category.EMPTY_EXPENSE.copy(
                                        name = name,
                                        icon = CommonAndroidR.drawable.ic_category_cafe,
                                        color = android.R.color.black,
                                    ),
                                sum = BigDecimal.valueOf((index + 1).toLong()),
                                type = TransactionType.EXPENSE,
                            )
                        },
                ),
            onBackClicked = {},
            onTransactionClicked = {},
        )
    }
}
