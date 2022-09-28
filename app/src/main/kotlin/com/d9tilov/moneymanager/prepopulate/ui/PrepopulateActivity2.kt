package com.d9tilov.moneymanager.prepopulate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.theme.MoneyManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: PrepopulateViewModel = viewModel()
            MoneyManagerTheme {
                val uiState by viewModel.uiState.collectAsState()
                PrepopulateScreen(uiState)
            }
        }
    }
}

@Composable
fun PrepopulateScreen(uiState: PrepopulateUiState) {
    var screenType by remember { mutableStateOf(PrepopulateScreen.CURRENCY) }
    Column {
        CurrencyListContainer(uiState.currencyList, modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Progress"
            )
            IconButton(onClick = {  },
                modifier = Modifier
                    .size(40.dp)
                    .padding(horizontal = 4.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)),
            ) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "content description",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            OutlinedButton(
                onClick = { },
                modifier = Modifier.height(height = 40.dp).wrapContentWidth(),
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

@Composable
fun CurrencyListContainer(currencyList: List<DomainCurrency>, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "currency")
    }
}

@Composable
fun BudgetContainer(budget: BudgetData, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "budget")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoneyManagerTheme {
        PrepopulateScreen(PrepopulateUiState())
    }
}

enum class PrepopulateScreen {
    CURRENCY,
    BUDGET
}
