package com.d9tilov.android.incomeexpense.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.d9tilov.android.common.android.utils.TRANSACTION_DATE_FORMAT
import com.d9tilov.android.common.android.utils.formatDate
import com.d9tilov.android.designsystem.EmptyListPlaceholder
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.EXPENSE
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.INCOME
import com.d9tilov.android.transaction.ui.TransactionItem
import com.d9tilov.android.transaction.ui.model.BaseTransaction
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun TransactionListLayout(
    listState: LazyListState,
    modifier: Modifier,
    transactions: Flow<PagingData<BaseTransaction>>,
    screenType: ScreenType,
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onDeleteTransactionConfirmClicked: (TransactionUiModel) -> Unit,
) {
    val lazyTransactionItems: LazyPagingItems<BaseTransaction> =
        transactions.collectAsLazyPagingItems()
    val captureScrollAnchor = rememberTransactionScrollAnchor(lazyTransactionItems, listState)
    val coroutineScope = rememberCoroutineScope()
    if (lazyTransactionItems.loadState.refresh is LoadState.NotLoading && lazyTransactionItems.itemCount == 0) {
        EmptyListPlaceholder(
            modifier = Modifier.fillMaxSize(),
            icon = painterResource(id = MoneyManagerIcons.EmptyPlaceholder),
            title =
                when (screenType) {
                    EXPENSE -> stringResource(id = R.string.transaction_empty_placeholder_expense_title)
                    INCOME -> stringResource(id = R.string.transaction_empty_placeholder_income_title)
                },
            subtitle = stringResource(id = R.string.transaction_empty_placeholder_subtitle),
        )
        return
    }
    if (lazyTransactionItems.loadState.refresh is LoadState.Error) {
        // handle error
    }
    val openRemoveDialog = remember { mutableStateOf<Pair<TransactionUiModel, SwipeToDismissBoxState>?>(null) }
    LazyColumn(modifier = modifier, state = listState) {
        transactionItems(
            lazyTransactionItems = lazyTransactionItems,
            onTransactionClicked = { transaction ->
                captureScrollAnchor(transaction.id, null)
                onTransactionClicked(transaction)
            },
            onRemoveRequested = { transaction, state -> openRemoveDialog.value = transaction to state },
        )
        item {
            if (lazyTransactionItems.loadState.append is LoadState.Loading) {
                // handle loading
            }
        }
    }
    SimpleDialog(
        show = openRemoveDialog.value != null,
        title = stringResource(R.string.transaction_delete_dialog_title),
        subtitle = stringResource(R.string.transaction_delete_dialog_subtitle),
        dismissButton = stringResource(com.d9tilov.android.common.android.R.string.cancel),
        confirmButton = stringResource(com.d9tilov.android.common.android.R.string.delete),
        onConfirm = {
            openRemoveDialog.value?.let { (transaction, _) ->
                captureScrollAnchor(null, transaction.id)
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

private fun BaseTransaction.listKey(): String =
    if (itemType == BaseTransaction.HEADER) {
        "header:$date"
    } else {
        "transaction:${(this as TransactionUiModel).id}"
    }

@Composable
private fun rememberTransactionScrollAnchor(
    lazyTransactionItems: LazyPagingItems<BaseTransaction>,
    listState: LazyListState,
): (Long?, Long?) -> Unit {
    var scrollAnchor by rememberSaveable { mutableStateOf<ScrollAnchor?>(null) }
    val snapshot = lazyTransactionItems.itemSnapshotList
    LaunchedEffect(snapshot) {
        scrollAnchor?.let { anchor ->
            restoreTransactionScrollAnchor(
                lazyTransactionItems = lazyTransactionItems,
                listState = listState,
                anchor = anchor,
                snapshotHash = snapshot.items.hashCode(),
                onRestored = { scrollAnchor = null },
            )
        }
    }

    return { preferredId, excludedId ->
        findTransactionScrollAnchor(
            snapshot = snapshot.items,
            visibleItems = listState.layoutInfo.visibleItemsInfo,
            preferredId = preferredId,
            excludedId = excludedId,
        )?.let { scrollAnchor = it }
    }
}

private suspend fun restoreTransactionScrollAnchor(
    lazyTransactionItems: LazyPagingItems<BaseTransaction>,
    listState: LazyListState,
    anchor: ScrollAnchor,
    snapshotHash: Int,
    onRestored: () -> Unit,
) {
    if (snapshotHash == anchor.snapshotHash) return
    val latestSnapshot = lazyTransactionItems.itemSnapshotList
    val anchorIndex = latestSnapshot.items.indexOfFirst { it.listKey() == anchor.itemKey }
    if (anchorIndex >= 0) {
        listState.scrollToItem(anchorIndex, anchor.scrollOffset)
        onRestored()
    } else if (lazyTransactionItems.loadState.append is LoadState.NotLoading) {
        val appendState = lazyTransactionItems.loadState.append as LoadState.NotLoading
        if (appendState.endOfPaginationReached) {
            onRestored()
        } else if (lazyTransactionItems.itemCount > 0) {
            lazyTransactionItems[lazyTransactionItems.itemCount - 1]
        }
    }
}

internal fun findTransactionScrollAnchor(
    snapshot: List<BaseTransaction>,
    visibleItems: List<LazyListItemInfo>,
    preferredId: Long?,
    excludedId: Long?,
): ScrollAnchor? {
    val transactionsByKey =
        snapshot
            .filterIsInstance<TransactionUiModel>()
            .associateBy { it.listKey() }
    val visibleTransactions =
        visibleItems.mapNotNull { itemInfo ->
            val transaction = transactionsByKey[itemInfo.key]
            if (transaction?.id == excludedId) null else transaction?.let { it to itemInfo }
        }
    val (transaction, itemInfo) =
        visibleTransactions.firstOrNull { (transaction, _) -> transaction.id == preferredId }
            ?: visibleTransactions.firstOrNull()
            ?: return null
    return ScrollAnchor(
        itemKey = transaction.listKey(),
        scrollOffset = -itemInfo.offset,
        snapshotHash = snapshot.hashCode(),
    )
}

private fun LazyListScope.transactionItems(
    lazyTransactionItems: LazyPagingItems<BaseTransaction>,
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onRemoveRequested: (TransactionUiModel, SwipeToDismissBoxState) -> Unit,
) {
    for (index in 0 until lazyTransactionItems.itemCount) {
        val currentItem = lazyTransactionItems.peek(index)
        currentItem?.let { tr ->
            if (tr.itemType == BaseTransaction.HEADER) {
                stickyHeader(key = tr.listKey()) {
                    TransactionDateHeader(
                        modifier = Modifier.fillParentMaxWidth(),
                        transaction = currentItem,
                    )
                }
            } else {
                item(key = currentItem.listKey()) {
                    val item = lazyTransactionItems[index] as TransactionUiModel
                    DismissibleTransactionItem(
                        item = item,
                        onTransactionClicked = onTransactionClicked,
                        onRemoveRequested = onRemoveRequested,
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionDateHeader(
    modifier: Modifier,
    transaction: BaseTransaction,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = formatDate(transaction.date, TRANSACTION_DATE_FORMAT),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun DismissibleTransactionItem(
    item: TransactionUiModel,
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onRemoveRequested: (TransactionUiModel, SwipeToDismissBoxState) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(SwipeToDismissBoxValue.Settled)
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onRemoveRequested(item, dismissState)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            TransactionDismissBackground(dismissState)
        },
        content = {
            TransactionItem(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onTransactionClicked(item)
                        },
                transaction = item,
            )
        },
    )
}

@Composable
private fun TransactionDismissBackground(dismissState: SwipeToDismissBoxState) {
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
