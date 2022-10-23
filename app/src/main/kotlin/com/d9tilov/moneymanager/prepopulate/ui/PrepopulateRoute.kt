package com.d9tilov.moneymanager.prepopulate.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PrepopulateRoute(prepopulateViewModel: PrepopulateViewModel = viewModel()) {
    val uiState: PrepopulateUiState by prepopulateViewModel.uiState.collectAsState()
    PrepopulateRoute(uiState)
}

@Composable
fun PrepopulateRoute(uiState: PrepopulateUiState) {
    Column {
        var screenType: PrepopulateScreen by remember { mutableStateOf(PrepopulateScreen.CURRENCY) }
        var progress by remember { mutableStateOf(1.0f / PrepopulateScreen.values().size) }
        val animatedProgress = animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value
        when (screenType) {
            PrepopulateScreen.CURRENCY -> CurrencyListScreen(
                uiState.currencyUiState,
                Modifier.weight(1f)
            )
            PrepopulateScreen.BUDGET -> BudgetScreen(uiState.budgetUiState, Modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = animatedProgress, modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.5f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        screenType = when (screenType) {
                            PrepopulateScreen.BUDGET -> PrepopulateScreen.CURRENCY
                            else -> PrepopulateScreen.CURRENCY
                        }
                        progress = (screenType.ordinal * 1.0f + 1) / PrepopulateScreen.values().size
                    }, modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .border(
                            1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                        )
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "content description",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                OutlinedButton(
                    onClick = {
                        screenType = when (screenType) {
                            PrepopulateScreen.CURRENCY -> PrepopulateScreen.BUDGET
                            else -> PrepopulateScreen.BUDGET
                        }
                        progress = (screenType.ordinal * 1.0f + 1) / PrepopulateScreen.values().size
                    },
                    modifier = Modifier
                        .height(height = 40.dp)
                        .wrapContentWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Next")
                        Icon(
                            Icons.Default.ArrowDropUp,
                            contentDescription = "content description",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
