package com.d9tilov.android.statistics.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.designsystem.ButtonSelector
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCategoryType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuChartModel
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCurrency
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatistics
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.ui.vm.PeriodUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsMenuState
import com.d9tilov.android.statistics.ui.vm.StatisticsUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsViewModel
import com.d9tilov.android.statistics_ui.R

@Composable
fun StatisticsRoute(
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    val uiState: StatisticsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold { paddingValues ->
        StatisticsScreen(
            modifier = Modifier.padding(paddingValues), state = uiState
        )
    }
}

@Composable
fun StatisticsScreen(
    modifier: Modifier,
    state: StatisticsUiState,
    onPeriodClick: () -> Unit = {},
    onMenuClick: (type: StatisticsMenuType) -> Unit = {},
) {
    Column {
        StatisticsPeriodSelector(state = state.periodState, onClick = onPeriodClick)
        StatisticsMenuSelector(state = state.statisticsMenuState, onClick = onMenuClick)
    }
}

@Composable
fun StatisticsPeriodSelector(modifier: Modifier = Modifier, state: PeriodUiState, onClick: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (item in state.periods) {
            ButtonSelector(
                enabled = state.selectedPeriod == item,
                text = { Text(text = stringResource(item.name)) },
                onClick = onClick
            )
        }
    }
}

@Composable
fun StatisticsMenuSelector(
    modifier: Modifier = Modifier,
    state: StatisticsMenuState,
    onClick: (type: StatisticsMenuType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (itemEntry in state.menuMap) {
            when (val type = itemEntry.value.menuType) {
                StatisticsMenuType.CURRENCY -> Text(
                    text = CurrencyUtils.getCurrencyIcon((itemEntry.value as StatisticsMenuCurrency).currencyCode),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.statistics_menu_item_icon_size))
                        .clickable { onClick(type) },
                    fontSize = 30.sp,
                )

                StatisticsMenuType.CHART ->
                    StatisticMenuIcon(
                        (itemEntry.value as StatisticsMenuChartModel).iconId
                    ) { onClick.invoke(type) }

                StatisticsMenuType.CATEGORY_TYPE ->
                    StatisticMenuIcon(
                        (itemEntry.value as StatisticsMenuCategoryType).iconId
                    ) { onClick.invoke(type) }

                StatisticsMenuType.TRANSACTION_TYPE ->
                    StatisticMenuIcon(
                        (itemEntry.value as StatisticsMenuTransactionType).iconId
                    ) { onClick.invoke(type) }

                StatisticsMenuType.STATISTICS -> StatisticMenuIcon(
                    (itemEntry.value as StatisticsMenuInStatistics).iconId
                ) { onClick.invoke(type) }
            }
        }
    }
}

@Composable
fun StatisticMenuIcon(iconId: Int, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(dimensionResource(R.dimen.statistics_menu_item_icon_size))
            .clickable { onClick() },
        imageVector = ImageVector.vectorResource(iconId),
        tint = Color.Unspecified,
        contentDescription = null,
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultCategoryIconGridPreview() {
    MoneyManagerTheme {
        StatisticsScreen(Modifier, StatisticsUiState())
    }
}
