package com.d9tilov.moneymanager.prepopulate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.d9tilov.moneymanager.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrepopulateScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
     PrepopulateScreen()
}

sealed class PrepopulateScreen(val id: Int, @StringRes val title: Int) {

    object CurrencyScreen : PrepopulateScreen(0, R.string.title_prepopulate_currency)
    object BudgetScreen : PrepopulateScreen(1, R.string.title_prepopulate_budget)

    companion object {
        const val SCREEN_COUNT = 2

        fun Int.fromScreenId() = when (this) {
            0 -> CurrencyScreen
            1 -> BudgetScreen
            else -> throw IllegalArgumentException("Wrong screen id: $this")
        }
    }
}
