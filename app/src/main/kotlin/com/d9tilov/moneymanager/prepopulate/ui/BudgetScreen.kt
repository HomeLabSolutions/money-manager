package com.d9tilov.moneymanager.prepopulate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.d9tilov.moneymanager.budget.vm.BudgetUiState

@Composable
fun BudgetScreen(budgetUiState: BudgetUiState, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "budget")
    }
}
