package com.d9tilov.android.transaction.regular.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.EmptyListPlaceholder
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.ui.vm.RegularTransactionListState
import com.d9tilov.android.transaction.regular.ui.vm.RegularTransactionListViewModel
import kotlinx.coroutines.launch

private const val PREVIEW_TRANSACTION_COUNT = 18

@Composable
fun RegularTransactionListRoute(
    viewModel: RegularTransactionListViewModel = hiltViewModel(),
    onAddClicked: (TransactionType, Long) -> Unit,
    onItemClicked: (RegularTransaction) -> Unit,
    clickBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RegularTransactionListScreen(
        uiState = uiState,
        onAddClicked = { onAddClicked(uiState.transactionType, NO_ID) },
        onTransactionClicked = onItemClicked,
        onDeleteTransactionConfirmClicked = viewModel::removeTransaction,
        onBackClicked = clickBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTransactionListScreen(
    uiState: RegularTransactionListState,
    onTransactionClicked: (currency: RegularTransaction) -> Unit = {},
    onDeleteTransactionConfirmClicked: (RegularTransaction) -> Unit,
    onAddClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val openRemoveDialog = remember { mutableStateOf<Pair<RegularTransaction, SwipeToDismissBoxState>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MmTopAppBar(
                titleRes =
                    when (uiState.transactionType) {
                        TransactionType.EXPENSE -> R.string.profile_item_regular_expenses_title
                        TransactionType.INCOME -> R.string.profile_item_regular_incomes_title
                    },
                onNavigationClick = onBackClicked,
                actionIcon = MoneyManagerIcons.ActionAdd,
                onActionClick = onAddClicked,
            )
        },
    ) { padding: PaddingValues ->
        if (uiState.regularTransactions.isEmpty()) {
            EmptyListPlaceholder(
                modifier = Modifier.fillMaxSize(),
                icon = painterResource(id = MoneyManagerIcons.EmptyRegularPlaceholder),
                title =
                    when (uiState.transactionType) {
                        TransactionType.EXPENSE -> {
                            stringResource(
                                id = R.string.transaction_empty_placeholder_regular_expense_title,
                            )
                        }

                        TransactionType.INCOME -> {
                            stringResource(
                                id = R.string.transaction_empty_placeholder_regular_income_title,
                            )
                        }
                    },
            )
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.consumeWindowInsets(padding),
            ) {
                items(items = uiState.regularTransactions, key = { item -> item.id }) { item ->
                    DismissibleRegularTransactionItem(
                        item = item,
                        onTransactionClicked = onTransactionClicked,
                        onRemoveRequested = { transaction, state -> openRemoveDialog.value = transaction to state },
                    )
                }
            }
        }
    }
    SimpleDialog(
        show = openRemoveDialog.value != null,
        title = stringResource(R.string.regular_transaction_delete_dialog_title),
        subtitle = stringResource(R.string.regular_transaction_delete_dialog_subtitle),
        dismissButton = stringResource(com.d9tilov.android.common.android.R.string.cancel),
        confirmButton = stringResource(com.d9tilov.android.common.android.R.string.delete),
        onConfirm = {
            openRemoveDialog.value?.let { (transaction, _) ->
                onDeleteTransactionConfirmClicked(transaction)
            }
            openRemoveDialog.value = null
        },
        onDismiss = {
            openRemoveDialog.value?.let { (_, state) ->
                coroutineScope.launch { state.reset() }
            }
            openRemoveDialog.value = null
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissibleRegularTransactionItem(
    item: RegularTransaction,
    onTransactionClicked: (RegularTransaction) -> Unit,
    onRemoveRequested: (RegularTransaction, SwipeToDismissBoxState) -> Unit,
) {
    val dismissState =
        rememberSwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
        )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onRemoveRequested(item, dismissState)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            RegularTransactionDismissBackground(dismissState)
        },
        content = {
            RegularTransactionItem(
                transaction = item,
                onClick = { onTransactionClicked(item) },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegularTransactionDismissBackground(dismissState: SwipeToDismissBoxState) {
    val backgroundColor by animateColorAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
            MaterialTheme.colorScheme.error
        } else {
            Color.Transparent
        },
        label = "",
    )
    val iconScale by animateFloatAsState(
        targetValue =
            if (dismissState.targetValue ==
                SwipeToDismissBoxValue.Settled
            ) {
                0.0f
            } else {
                1.3f
            },
        label = "",
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(
                horizontal =
                    dimensionResource(
                        id = com.d9tilov.android.designsystem.R.dimen.padding_medium,
                    ),
            ),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            modifier = Modifier.scale(iconScale),
            imageVector = MoneyManagerIcons.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onError,
        )
    }
}

@Preview
@Composable
fun DefaultRegularTransactionListPreview() {
    RegularTransactionListScreen(
        uiState =
            RegularTransactionListState(
                transactionType = TransactionType.EXPENSE,
                regularTransactions =
                    List(PREVIEW_TRANSACTION_COUNT) { index ->
                        val id = (index + 1).toLong()
                        RegularTransaction.EMPTY.copy(id = id, category = mockCategory(id, "Category$id"))
                    },
            ),
        onAddClicked = {},
        onTransactionClicked = {},
        onBackClicked = {},
        onDeleteTransactionConfirmClicked = {},
    )
}

private fun mockCategory(
    id: Long,
    name: String,
) = Category.EMPTY_EXPENSE.copy(
    id = id,
    name = name,
    icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
    color = android.R.color.holo_blue_light,
)
