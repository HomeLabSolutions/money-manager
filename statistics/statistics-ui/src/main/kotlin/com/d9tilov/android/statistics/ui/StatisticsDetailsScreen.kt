package com.d9tilov.android.statistics.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.UiConstants.ALPHA
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsViewModel
import com.d9tilov.android.statistics_ui.R
import com.d9tilov.android.transaction.domain.model.Transaction
import java.math.BigDecimal

@Composable
fun StatisticsDetailsRoute(
    viewModel: StatisticsDetailsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
) {
    val uiState: StatisticsDetailsUiState by viewModel.uiState.collectAsState(StatisticsDetailsUiState())
    StatisticsDetailsScreen(state = uiState, onBackClicked = onBackClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsDetailsScreen(
    modifier: Modifier = Modifier,
    state: StatisticsDetailsUiState,
    onBackClicked: () -> Unit,
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
                TransactionItem(Modifier.fillMaxWidth(), item)
            }
        }
    }
}

@Composable
fun TransactionItem(
    modifier: Modifier,
    transaction: Transaction,
) {
    val context = LocalContext.current
    ConstraintLayout(
        modifier =
            modifier
                .height(72.dp)
                .padding(horizontal = 32.dp),
    ) {
        val (idIcon, idTitle, idDescription, idRegularIcon, idInStatisticsIcon, idSum, idDivider) = createRefs()
        Icon(
            modifier =
                Modifier
                    .constrainAs(idIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }.size(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.category_item_icon_size)),
            imageVector = ImageVector.vectorResource(id = transaction.category.icon),
            contentDescription = "Transaction",
            tint = Color(ContextCompat.getColor(context, transaction.category.color)),
        )
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp)
                    .constrainAs(idTitle) {
                        top.linkTo(idIcon.top)
                        bottom.linkTo(idIcon.bottom)
                        start.linkTo(idIcon.end)
                    },
            text = transaction.category.name,
            style = MaterialTheme.typography.displayLarge,
            fontSize =
                dimensionResource(
                    id = com.d9tilov.android.designsystem.R.dimen.income_expense_name_text_size,
                ).value.sp,
            maxLines = 1,
            color = Color(ContextCompat.getColor(context, transaction.category.color)),
            overflow = TextOverflow.Ellipsis,
        )
        if (transaction.description.isNotEmpty()) {
            Text(
                modifier =
                    Modifier
                        .padding(start = 16.dp, bottom = 4.dp)
                        .constrainAs(idDescription) {
                            top.linkTo(idTitle.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(idTitle.start)
                        },
                text = transaction.description,
                style = MaterialTheme.typography.bodySmall,
                fontSize =
                    dimensionResource(
                        id = com.d9tilov.android.designsystem.R.dimen.income_expense_name_description_text_size,
                    ).value.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        if (transaction.isRegular) {
            Icon(
                modifier =
                    Modifier
                        .constrainAs(idRegularIcon) {
                            top.linkTo(idIcon.top)
                            end.linkTo(idIcon.start)
                        }.size(
                            dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size),
                        ),
                imageVector = MoneyManagerIcons.Repeat,
                contentDescription = "RegularTransaction",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
        if (!transaction.inStatistics) {
            Icon(
                modifier =
                    Modifier
                        .padding(top = 4.dp)
                        .constrainAs(idInStatisticsIcon) {
                            top.linkTo(idRegularIcon.bottom)
                            end.linkTo(idIcon.start)
                        }.size(
                            dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size),
                        ),
                imageVector = ImageVector.vectorResource(MoneyManagerIcons.InStatisticsTransaction),
                contentDescription = "InStatisticsTransaction",
                tint = MaterialTheme.colorScheme.error,
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .constrainAs(idSum) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            ComposeCurrencyView(
                value = transaction.sum.toString(),
                valueStyle = MaterialTheme.typography.headlineSmall,
                symbol = transaction.currencyCode.getSymbolByCode(),
                symbolStyle = MaterialTheme.typography.labelLarge,
            )
            ComposeCurrencyView(
                value = transaction.usdSum.toString(),
                valueStyle = MaterialTheme.typography.bodyLarge,
                symbol = DEFAULT_CURRENCY_SYMBOL,
                symbolStyle = MaterialTheme.typography.bodyMedium,
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier =
                Modifier
                    .alpha(ALPHA)
                    .constrainAs(idDivider) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
        )
    }
}

@Preview(showBackground = true)
@Composable
@Suppress("MagicNumber")
fun StatisticsDetailsScreenPreview() {
    MoneyManagerTheme {
        StatisticsDetailsScreen(
            state =
                StatisticsDetailsUiState(
                    categoryName = "MyCategory",
                    transactions =
                        listOf(
                            Transaction.EMPTY.copy(
                                id = 1,
                                category =
                                    Category.EMPTY_EXPENSE.copy(
                                        name = "Category1",
                                        icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                        color = android.R.color.black,
                                    ),
                                sum = BigDecimal(1),
                                type = TransactionType.EXPENSE,
                            ),
                            Transaction.EMPTY.copy(
                                id = 2,
                                category =
                                    Category.EMPTY_EXPENSE.copy(
                                        name = "Category2",
                                        icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                        color = android.R.color.black,
                                    ),
                                sum = BigDecimal(2),
                                type = TransactionType.EXPENSE,
                            ),
                            Transaction.EMPTY.copy(
                                id = 3,
                                category =
                                    Category.EMPTY_EXPENSE.copy(
                                        name = "Category3",
                                        icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                        color = android.R.color.black,
                                    ),
                                sum = BigDecimal(3),
                                type = TransactionType.EXPENSE,
                            ),
                        ),
                ),
            onBackClicked = {},
        )
    }
}
