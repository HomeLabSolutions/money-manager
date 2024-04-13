package com.d9tilov.moneymanager.prepopulate

import android.app.Activity
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.budget.ui.BudgetScreen
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.currency.ui.CurrencyListScreen
import com.d9tilov.android.designsystem.MoneyManagerIcons
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
        uiState,
        { currency -> prepopulateViewModel.changeCurrency(currency.code) },
        { budget -> prepopulateViewModel.changeBudgetAmount(budget) },
        {
            prepopulateViewModel.saveBudgetAmountAndComplete()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrepopulateScreen(
    uiState: PrepopulateUiState,
    clickCallback: (currency: DomainCurrency) -> Unit,
    onBudgetInputChanged: (String) -> Unit,
    onBudgetSaveAndComplete: () -> Unit
) {
    var screenTypeId by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(screenTypeId.fromScreenId().title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    ClickableText(
                        text = AnnotatedString(stringResource(R.string.toolbar_menu_skip)),
                        modifier = Modifier.padding(8.dp),
                        onClick = { onBudgetSaveAndComplete() },
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                screenType = screenTypeId.fromScreenId(),
                onBudgetSave = onBudgetSaveAndComplete
            ) { newScreen -> screenTypeId = newScreen.id }
        }
    ) { padding: PaddingValues ->
        Column(modifier = Modifier.padding(padding)) {
            when (screenTypeId.fromScreenId()) {
                PrepopulateScreen.CurrencyScreen ->
                    CurrencyListScreen(
                    uiState.currencyUiState,
                    Modifier.weight(1f),
                    false,
                    clickCallback
                )
                PrepopulateScreen.BudgetScreen -> BudgetScreen(
                        uiState = uiState.budgetUiState,
                        showInPrepopulate = true,
                        onBudgetInputChanged = onBudgetInputChanged
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
    onScreenChanged: (PrepopulateScreen) -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var progress by remember { mutableFloatStateOf((screenType.id + 1f) / PrepopulateScreen.SCREEN_COUNT) }
        ProgressIndicator(
            indicatorProgress = progress,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(FRACTION)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val newScreenType = when (screenType) {
                        is PrepopulateScreen.BudgetScreen -> PrepopulateScreen.CurrencyScreen
                        else -> PrepopulateScreen.CurrencyScreen
                    }
                    progress =
                        (newScreenType.id * 1.0f + 1) / PrepopulateScreen.SCREEN_COUNT
                    onScreenChanged(newScreenType)
                }, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(SHAPE_RADIUS)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = MoneyManagerIcons.ArrowDropDown,
                    contentDescription = "content description",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            OutlinedButton(
                onClick = {
                    val newScreenType = when (screenType) {
                        PrepopulateScreen.CurrencyScreen -> PrepopulateScreen.BudgetScreen
                        else -> {
                            onBudgetSave()
                            PrepopulateScreen.BudgetScreen
                        }
                    }
                    progress =
                        (newScreenType.id * 1.0f + 1) / PrepopulateScreen.SCREEN_COUNT
                    onScreenChanged(newScreenType)
                },
                modifier = Modifier
                    .height(height = 40.dp)
                    .wrapContentWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(SHAPE_RADIUS),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Next")
                    Icon(
                        imageVector = MoneyManagerIcons.ArrowDropUp,
                        contentDescription = "content description",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressIndicator(indicatorProgress: Float, modifier: Modifier) {
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing), label = ""
    )
    LinearProgressIndicator(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp)), // Rounded edges
        progress = { progressAnimation }
    )
}

sealed class PrepopulateScreen(val id: Int, @StringRes val title: Int) {

    data object CurrencyScreen : PrepopulateScreen(0, R.string.title_prepopulate_currency)
    data object BudgetScreen : PrepopulateScreen(1, R.string.title_prepopulate_budget)

    companion object {
        const val SCREEN_COUNT = 2

        fun Int.fromScreenId() = when (this) {
            0 -> CurrencyScreen
            1 -> BudgetScreen
            else -> throw IllegalArgumentException("Wrong screen id: $this")
        }
    }
}
