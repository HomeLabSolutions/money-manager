package com.d9tilov.android.statistics.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.CurrencyConstants
import com.d9tilov.android.core.constants.UiConstants.ALPHA
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toStandardStringDate
import com.d9tilov.android.designsystem.ButtonSelector
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.DateRangePickerModal
import com.d9tilov.android.designsystem.EmptyListPlaceholder
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.ProgressIndicator
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.charts.PieChart
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatisticsType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.ui.model.StatisticsPeriodModel
import com.d9tilov.android.statistics.ui.model.TransactionDetailsChartModel
import com.d9tilov.android.statistics.ui.model.chart.Pie
import com.d9tilov.android.statistics.ui.vm.ChartState
import com.d9tilov.android.statistics.ui.vm.DetailsSpentInPeriodState
import com.d9tilov.android.statistics.ui.vm.DetailsTransactionListState
import com.d9tilov.android.statistics.ui.vm.PeriodUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsMenuState
import com.d9tilov.android.statistics.ui.vm.StatisticsUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsViewModel
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

private const val ANIMATION_DURATION = 300

@Composable
fun StatisticsRoute(
    viewModel: StatisticsViewModel = hiltViewModel(),
    onTransactionClicked: (TransactionDetailsChartModel, LocalDateTime, LocalDateTime) -> Unit,
) {
    val uiState: StatisticsUiState by viewModel.uiState.collectAsState(StatisticsUiState())
    Scaffold { paddingValues ->
        StatisticsScreen(
            modifier = Modifier.padding(paddingValues),
            state = uiState,
            onPeriodClick = { viewModel.updatePeriod(it) },
            onMenuClick = { viewModel.onMenuClick(it) },
            onTransactionClicked = {
                onTransactionClicked(
                    it,
                    uiState.periodState.selectedPeriod.from,
                    uiState.periodState.selectedPeriod.to,
                )
            },
            onPrevClicked = { viewModel.onPeriodArrowClicked(false) },
            onNextClicked = { viewModel.onPeriodArrowClicked(true) },
        )
    }
}

@Composable
fun StatisticsScreen(
    modifier: Modifier,
    state: StatisticsUiState,
    onPeriodClick: (period: StatisticsPeriodModel) -> Unit,
    onMenuClick: (type: StatisticsMenuType) -> Unit,
    onTransactionClicked: (TransactionDetailsChartModel) -> Unit,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    val showDatePicker = remember { mutableStateOf(false) }
    Column(modifier) {
        StatisticsPeriodSelector(
            state = state.periodState,
            onPeriodClick = { period: StatisticsPeriodModel ->
                if (period == StatisticsPeriodModel.CUSTOM()) {
                    showDatePicker.value = true
                }
                onPeriodClick(period)
            },
        )
        StatisticsMenuSelector(state = state.statisticsMenuState, onClick = onMenuClick)

        Column(modifier = Modifier.weight(2f)) {
            var selectedIndex by remember { mutableIntStateOf(-1) }
            StatisticsChart(
                Modifier.fillMaxSize(),
                periodUiState = state.periodState,
                periodStr =
                    if (state.periodState.selectedPeriod is StatisticsPeriodModel.DAY) {
                        state.periodState.selectedPeriod.from
                            .toStandardStringDate()
                    } else {
                        "${state.periodState.selectedPeriod.from.toStandardStringDate()} " +
                            "- ${state.periodState.selectedPeriod.to.toStandardStringDate()}"
                    },
                pieData =
                    state.chartState.pieData.mapIndexed { index, pie ->
                        pie.copy(selected = index == selectedIndex)
                    },
                { selectedIndex = it },
                onPrevClicked,
                onNextClicked,
            )
        }
        StatisticsList(
            modifier = Modifier.weight(1f),
            state = state.detailsTransactionListState,
            transactionType = state.statisticsMenuState.transactionType,
            onItemClick = {
                onTransactionClicked(
                    TransactionDetailsChartModel(
                        it.category.id,
                        state.statisticsMenuState.inStatistics == StatisticsMenuInStatisticsType.InStatisticsType,
                    ),
                )
            },
        )
    }

    if (showDatePicker.value) {
        DateRangePickerModal(
            onDateRangeSelected = { pairPeriod ->
                onPeriodClick(
                    StatisticsPeriodModel.CUSTOM(
                        pairPeriod.first.toLocalDateTime(),
                        pairPeriod.second.toLocalDateTime(),
                    ),
                )
            },
            onDismiss = { showDatePicker.value = false },
        )
    }
}

@Composable
fun StatisticsPeriodSelector(
    modifier: Modifier = Modifier,
    state: PeriodUiState,
    onPeriodClick: (period: StatisticsPeriodModel) -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small),
                    horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small),
                ),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        for (item: StatisticsPeriodModel in state.periods) {
            ButtonSelector(
                enabled = state.selectedPeriod.name == item.name,
                text = { Text(text = stringResource(item.name)) },
                onClick = { onPeriodClick(item) },
            )
        }
    }
}

@Composable
fun StatisticsMenuSelector(
    modifier: Modifier = Modifier,
    state: StatisticsMenuState,
    onClick: (type: StatisticsMenuType) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = CurrencyUtils.getCurrencyIcon(state.currency.currencyCode),
            modifier =
                Modifier
                    .size(dimensionResource(R.dimen.statistics_menu_item_icon_size))
                    .clickable { onClick(StatisticsMenuType.CURRENCY) },
            fontSize = 30.sp,
        )
        StatisticMenuIcon(state.chartType.iconId) { onClick(StatisticsMenuType.CHART) }
        StatisticMenuIcon(state.transactionType.iconId) { onClick(StatisticsMenuType.TRANSACTION_TYPE) }
        StatisticMenuIcon(state.inStatistics.iconId) { onClick(StatisticsMenuType.STATISTICS) }
    }
}

@Composable
fun StatisticMenuIcon(
    iconId: Int,
    onClick: () -> Unit,
) {
    Icon(
        modifier =
            Modifier
                .size(dimensionResource(R.dimen.statistics_menu_item_icon_size))
                .clickable { onClick() },
        imageVector = ImageVector.vectorResource(iconId),
        tint = Color.Unspecified,
        contentDescription = null,
    )
}

@Composable
fun StatisticsChart(
    modifier: Modifier,
    periodUiState: PeriodUiState,
    periodStr: String,
    pieData: List<Pie>,
    onPieSelected: (Int) -> Unit,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    val floatSpec =
        spring<Float>(
            dampingRatio = .3f,
            stiffness = Spring.StiffnessLow,
        )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (periodUiState.showPrevArrow) {
            IconButton(
                onClick = onPrevClicked,
            ) {
                Icon(
                    imageVector = MoneyManagerIcons.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(dimensionResource(R.dimen.statistics_item_icon_size)),
                )
            }
        }

        val modifier =
            Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(
                    if (periodUiState.selectedPeriod is StatisticsPeriodModel.CUSTOM) {
                        dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large)
                    } else {
                        0.dp
                    },
                )
        if (pieData.isEmpty()) {
            EmptyListPlaceholder(
                modifier = modifier,
                icon = painterResource(id = MoneyManagerIcons.EmptyStatisticsPlaceholder),
                title = stringResource(id = R.string.statistics_no_data),
            )
        } else {
            PieChart(
                modifier = modifier,
                data = pieData,
                centerLabel = periodStr,
                onPieClick = {
                    println("${it.label} Clicked")
                    val pieIndex = pieData.indexOf(it)
                    onPieSelected(pieIndex)
                },
                selectedScale = 1.2f,
                spaceDegreeAnimEnterSpec = floatSpec,
                colorAnimEnterSpec = tween(ANIMATION_DURATION),
                scaleAnimEnterSpec = floatSpec,
                colorAnimExitSpec = tween(ANIMATION_DURATION),
                scaleAnimExitSpec = tween(ANIMATION_DURATION),
                spaceDegreeAnimExitSpec = tween(ANIMATION_DURATION),
                selectedPaddingDegree = 0f,
                style = Pie.Style.Stroke(48.dp),
            )
        }
        if (periodUiState.showNextArrow) {
            IconButton(
                onClick = onNextClicked,
            ) {
                Icon(
                    imageVector = MoneyManagerIcons.ArrowForward,
                    contentDescription = "Forward",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(dimensionResource(R.dimen.statistics_item_icon_size)),
                )
            }
        }
    }
}

@Composable
fun StatisticsList(
    modifier: Modifier = Modifier,
    state: DetailsTransactionListState,
    transactionType: StatisticsMenuTransactionType,
    onItemClick: (currency: TransactionChartModel) -> Unit,
) {
    Column(modifier = modifier) {
        val stateList = rememberLazyListState()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text =
                    if (transactionType == StatisticsMenuTransactionType.Expense) {
                        stringResource(R.string.statistics_expense_info_period_title)
                    } else {
                        stringResource(R.string.statistics_income_info_period_title)
                    },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier =
                    Modifier
                        .padding(end = 8.dp)
                        .alignByBaseline(),
            )
            ComposeCurrencyView(
                modifier = Modifier.alignByBaseline(),
                value = state.amount.value,
                symbol = state.amount.currencySymbol,
                valueStyle = MaterialTheme.typography.headlineSmall,
                symbolStyle = MaterialTheme.typography.labelLarge,
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = stateList,
        ) {
            items(items = state.transactions, key = { item -> item.category.id }) { item ->
                TransactionStatisticsItem(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) },
                    item,
                )
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
fun TransactionStatisticsItem(
    modifier: Modifier,
    transaction: TransactionChartModel,
) {
    val context = LocalContext.current
    val progressValue by remember { mutableFloatStateOf(transaction.percent.toFloat()) }
    ConstraintLayout(
        modifier =
            modifier
                .height(72.dp)
                .padding(horizontal = 32.dp),
    ) {
        val (idIcon, idTitle, idSum, progress, idDivider) = createRefs()
        Icon(
            modifier =
                Modifier
                    .constrainAs(idIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }.size(dimensionResource(id = R.dimen.statistics_item_icon_size)),
            imageVector = ImageVector.vectorResource(id = transaction.category.icon),
            contentDescription = "Transaction",
            tint = Color(ContextCompat.getColor(context, transaction.category.color)),
        )
        ProgressIndicator(
            indicatorProgress = progressValue,
            modifier =
                Modifier
                    .fillMaxWidth(0.4f)
                    .constrainAs(progress) {
                        top.linkTo(idIcon.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(idIcon.start)
                    },
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
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            color = Color(ContextCompat.getColor(context, transaction.category.color)),
            overflow = TextOverflow.Ellipsis,
        )
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
                value = transaction.sum.reduceScaleStr(),
                valueStyle = MaterialTheme.typography.headlineSmall,
                symbol = transaction.currencyCode.getSymbolByCode(),
                symbolStyle = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = "${transaction.percent.reduceScaleStr()}${
                    stringResource(
                        com.d9tilov.android.common.android.R.string.percent_sign,
                    )
                }",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
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
fun DefaultCategoryIconGridPreview() {
    MoneyManagerTheme {
        StatisticsScreen(
            Modifier,
            StatisticsUiState(
                chartState =
                    ChartState(
                        pieData = listOf(Pie.EMPTY),
                    ),
                detailsTransactionListState =
                    DetailsTransactionListState(
                        amount = DetailsSpentInPeriodState(),
                        transactions =
                            listOf(
                                TransactionChartModel(
                                    type = TransactionType.EXPENSE,
                                    category =
                                        Category.EMPTY_EXPENSE.copy(
                                            id = 1,
                                            name = "Category1",
                                            icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                            color = android.R.color.holo_red_dark,
                                        ),
                                    currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                                    sum = BigDecimal.TEN,
                                    percent = BigDecimal(0.4),
                                ),
                                TransactionChartModel(
                                    type = TransactionType.EXPENSE,
                                    category =
                                        Category.EMPTY_EXPENSE.copy(
                                            id = 2,
                                            name = "Category2",
                                            icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                            color = android.R.color.holo_blue_dark,
                                        ),
                                    currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                                    sum = BigDecimal.TEN,
                                    percent = BigDecimal(0.2),
                                ),
                                TransactionChartModel(
                                    type = TransactionType.EXPENSE,
                                    category =
                                        Category.EMPTY_EXPENSE.copy(
                                            id = 3,
                                            name = "Category3",
                                            icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                            color = android.R.color.holo_orange_dark,
                                        ),
                                    currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                                    sum = BigDecimal.TEN,
                                    percent = BigDecimal(0.2),
                                ),
                                TransactionChartModel(
                                    type = TransactionType.EXPENSE,
                                    category =
                                        Category.EMPTY_EXPENSE.copy(
                                            id = 4,
                                            name = "Category4",
                                            icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                            color = android.R.color.holo_orange_dark,
                                        ),
                                    currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                                    sum = BigDecimal.TEN,
                                    percent = BigDecimal(1.0),
                                ),
                                TransactionChartModel(
                                    type = TransactionType.EXPENSE,
                                    category =
                                        Category.EMPTY_EXPENSE.copy(
                                            id = 5,
                                            name = "Category5",
                                            icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                            color = android.R.color.holo_orange_dark,
                                        ),
                                    currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                                    sum = BigDecimal.TEN,
                                    percent = BigDecimal(0.8),
                                ),
                                TransactionChartModel(
                                    type = TransactionType.EXPENSE,
                                    category =
                                        Category.EMPTY_EXPENSE.copy(
                                            id = 6,
                                            name = "Category6",
                                            icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                                            color = android.R.color.holo_orange_dark,
                                        ),
                                    currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                                    sum = BigDecimal.TEN,
                                    percent = BigDecimal(0.7),
                                ),
                            ),
                    ),
            ),
            onPeriodClick = {},
            onMenuClick = {},
            onTransactionClicked = {},
            onPrevClicked = {},
            onNextClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
@Suppress("MagicNumber")
fun TransactionItemPreview() {
    MoneyManagerTheme {
        TransactionStatisticsItem(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { },
            TransactionChartModel(
                type = TransactionType.EXPENSE,
                category =
                    Category.EMPTY_EXPENSE.copy(
                        name = "Category1",
                        icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                        color = android.R.color.black,
                    ),
                currencyCode = CurrencyConstants.DEFAULT_CURRENCY_CODE,
                sum = BigDecimal.TEN,
                percent = BigDecimal(0.2),
            ),
        )
    }
}
