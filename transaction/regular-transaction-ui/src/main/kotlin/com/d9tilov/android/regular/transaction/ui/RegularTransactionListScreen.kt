package com.d9tilov.android.regular.transaction.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListState
import com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListViewModel


@Composable
fun RegularTransactionListRoute(
    viewModel: RegularTransactionListViewModel = hiltViewModel(),
    onAddClicked: (TransactionType, Long) -> Unit,
    clickBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RegularTransactionListScreen(
        uiState = uiState,
        onAddClicked = { onAddClicked.invoke(uiState.transactionType, NO_ID) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegularTransactionListScreen(
    uiState: RegularTransactionListState,
    onTransactionClicked: (currency: RegularTransaction) -> Unit = {},
    onAddClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            MmTopAppBar(
                titleRes = when (uiState.transactionType) {
                    TransactionType.EXPENSE -> com.d9tilov.android.regular_transaction_ui.R.string.profile_item_regular_expenses_title
                    TransactionType.INCOME -> com.d9tilov.android.regular_transaction_ui.R.string.profile_item_regular_incomes_title
                },
                actionIcon = MoneyManagerIcons.ActionAdd,
                onActionClick = onAddClicked
            )
        },
    ) { padding: PaddingValues ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.consumeWindowInsets(padding),
        ) {
            items(items = uiState.regularTransactions, key = { item -> item.id }) { item ->
                RegularTransactionItem(item, onTransactionClicked)
            }
        }
    }
}

@Composable
fun RegularTransactionItem(
    transaction: RegularTransaction,
    clickCallback: (currency: RegularTransaction) -> Unit,
) {
    val context = LocalContext.current
    Card(
        Modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_extra_small),
                horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_extra_small)
            )
            .clickable { clickCallback(transaction) }) {
        Row(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .fillMaxWidth()
                .height(84.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.padding(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.category_item_icon_small_size)),
                        imageVector = ImageVector.vectorResource(id = transaction.category.icon),
                        contentDescription = "Transaction",
                        tint = Color(ContextCompat.getColor(context, transaction.category.color))
                    )
                    androidx.compose.material.Text(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        text = transaction.category.name,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = dimensionResource(id = com.d9tilov.android.regular_transaction_ui.R.dimen.regular_transaction_category_name_text_size).value.sp,
                        maxLines = 1,
                        color = Color(ContextCompat.getColor(context, transaction.category.color)),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(text = transaction.executionPeriod.toString())
            }
            ComposeCurrencyView(
                modifier = Modifier.padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)),
                value = transaction.sum.toString(),
                valueSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_sum_small_text_size).value.sp,
                symbol = transaction.currencyCode.getSymbolByCode(),
                symbolSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_sign_small_text_size).value.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultRegularTransactionListPreview() {
    RegularTransactionListScreen(
        uiState = RegularTransactionListState(
            transactionType = TransactionType.EXPENSE,
            regularTransactions = listOf(
                RegularTransaction.EMPTY.copy(id = 1L, category = mockCategory(1L, "Category1")),
                RegularTransaction.EMPTY.copy(id = 2L, category = mockCategory(2L, "Category2")),
                RegularTransaction.EMPTY.copy(id = 3L, category = mockCategory(3L, "Category3")),
                RegularTransaction.EMPTY.copy(id = 4L, category = mockCategory(4L, "Category4")),
                RegularTransaction.EMPTY.copy(id = 5L, category = mockCategory(5L, "Category5")),
                RegularTransaction.EMPTY.copy(id = 6L, category = mockCategory(6L, "Category6")),
                RegularTransaction.EMPTY.copy(id = 7L, category = mockCategory(7L, "Category7")),
                RegularTransaction.EMPTY.copy(id = 8L, category = mockCategory(8L, "Category8")),
                RegularTransaction.EMPTY.copy(id = 9L, category = mockCategory(9L, "Category9")),
                RegularTransaction.EMPTY.copy(id = 10L, category = mockCategory(10L, "Category10")),
                RegularTransaction.EMPTY.copy(id = 11L, category = mockCategory(11L, "Category11")),
                RegularTransaction.EMPTY.copy(id = 12L, category = mockCategory(12L, "Category12")),
                RegularTransaction.EMPTY.copy(id = 13L, category = mockCategory(13L, "Category13")),
                RegularTransaction.EMPTY.copy(id = 14L, category = mockCategory(14L, "Category14")),
                RegularTransaction.EMPTY.copy(id = 15L, category = mockCategory(15L, "Category15")),
                RegularTransaction.EMPTY.copy(id = 16L, category = mockCategory(16L, "Category16")),
                RegularTransaction.EMPTY.copy(id = 17L, category = mockCategory(17L, "Category17")),
                RegularTransaction.EMPTY.copy(id = 18L, category = mockCategory(18L, "Category18")),
            )
        ),
        onAddClicked = {},
        onTransactionClicked = {}
    )
}

private fun mockCategory(id: Long, name: String) = Category.EMPTY_EXPENSE.copy(
    id = id,
    name = name,
    icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
    color = android.R.color.holo_blue_light,
)
