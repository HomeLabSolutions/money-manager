package com.d9tilov.moneymanager.prepopulate

import android.app.Activity
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.budget.ui.BudgetScreen
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.currency.ui.CurrencyListScreen
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.ProgressIndicator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.home.MainActivity
import com.d9tilov.moneymanager.prepopulate.PrepopulateScreen.Companion.fromScreenId

private const val SHAPE_RADIUS = 10
private const val FRACTION = 0.5f

@Composable
fun PrepopulateScreen(prepopulateViewModel: PrepopulateViewModel = hiltViewModel()) {
    val uiState: PrepopulateUiState by prepopulateViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    PrepopulateScreen(
        uiState = uiState,
        clickCallback = { currency -> prepopulateViewModel.changeCurrency(currency.code) },
        onBudgetInputChanged = { budget -> prepopulateViewModel.changeBudgetAmount(budget) },
        onAmountToSaveChanged = { budget -> prepopulateViewModel.changeAmountToSave(budget) },
        {
            prepopulateViewModel.saveBudgetAmountAndComplete()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrepopulateScreen(
    uiState: PrepopulateUiState,
    clickCallback: (currency: DomainCurrency) -> Unit,
    onBudgetInputChanged: (String) -> Unit,
    onAmountToSaveChanged: (String) -> Unit,
    onBudgetSaveAndComplete: () -> Unit,
) {
    var screenTypeId by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                TopAppBar(
                    title = { Text(text = stringResource(screenTypeId.fromScreenId().title)) },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                    actions = {
                        Text(
                            text = buildAnnotatedString { AnnotatedString(stringResource(R.string.toolbar_menu_skip)) },
                            modifier =
                                Modifier
                                    .padding(8.dp)
                                    .clickable { onBudgetSaveAndComplete() },
                        )
                    },
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                screenType = screenTypeId.fromScreenId(),
                onBudgetSave = onBudgetSaveAndComplete,
            ) { newScreen -> screenTypeId = newScreen.id }
        },
    ) { padding: PaddingValues ->
        Column(modifier = Modifier.padding(padding)) {
            when (screenTypeId.fromScreenId()) {
                PrepopulateScreen.CurrencyScreen ->
                    CurrencyListScreen(
                        uiState.currencyUiState,
                        Modifier.weight(1f),
                        false,
                        clickCallback,
                    )

                PrepopulateScreen.BudgetScreen ->
                    BudgetScreen(
                        uiState = uiState.budgetUiState,
                        showInPrepopulate = true,
                        onBudgetInputChanged = onBudgetInputChanged,
                        onAmountToSaveInputChanged = onAmountToSaveChanged,
                    )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier,
    screenType: PrepopulateScreen,
    onBudgetSave: () -> Unit,
    onScreenChanged: (PrepopulateScreen) -> Unit,
) {
    Row(
        modifier =
            modifier
                .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var progress by remember { mutableFloatStateOf((screenType.id + 1f) / PrepopulateScreen.SCREEN_COUNT) }
        ProgressIndicator(
            indicatorProgress = progress,
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(FRACTION),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    val newScreenType =
                        when (screenType) {
                            is PrepopulateScreen.BudgetScreen -> PrepopulateScreen.BudgetScreen
                            is PrepopulateScreen.CurrencyScreen -> PrepopulateScreen.CurrencyScreen
                        }
                    progress =
                        (newScreenType.id * 1.0f + 1) / PrepopulateScreen.SCREEN_COUNT
                    onScreenChanged(newScreenType)
                },
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(SHAPE_RADIUS),
                        ).size(40.dp),
            ) {
                Icon(
                    imageVector = MoneyManagerIcons.ArrowDropDown,
                    contentDescription = "content description",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            OutlinedButton(
                onClick = {
                    val newScreenType =
                        when (screenType) {
                            PrepopulateScreen.CurrencyScreen -> {
                                PrepopulateScreen.BudgetScreen
                            }
                            PrepopulateScreen.BudgetScreen -> {
                                onBudgetSave()
                                PrepopulateScreen.BudgetScreen
                            }
                        }
                    progress =
                        (newScreenType.id * 1.0f + 1) / PrepopulateScreen.SCREEN_COUNT
                    onScreenChanged(newScreenType)
                },
                modifier =
                    Modifier
                        .height(height = 40.dp)
                        .wrapContentWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(SHAPE_RADIUS),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(com.d9tilov.android.common.android.R.string.next))
                    Icon(
                        imageVector = MoneyManagerIcons.ArrowDropUp,
                        contentDescription = "content description",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

sealed class PrepopulateScreen(
    val id: Int,
    @field:StringRes val title: Int,
) {
    data object CurrencyScreen : PrepopulateScreen(0, R.string.title_prepopulate_currency)

    data object BudgetScreen : PrepopulateScreen(1, R.string.title_prepopulate_budget)

    companion object {
        const val SCREEN_COUNT = 2

        fun Int.fromScreenId() =
            when (this) {
                0 -> CurrencyScreen
                1 -> BudgetScreen
                else -> throw IllegalArgumentException("Wrong screen id: $this")
            }
    }
}
