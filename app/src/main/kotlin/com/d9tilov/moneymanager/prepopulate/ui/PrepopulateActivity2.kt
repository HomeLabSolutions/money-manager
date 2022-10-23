package com.d9tilov.moneymanager.prepopulate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PrepopulateRoute() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PrepopulateRoute()
}

enum class PrepopulateScreen {
    CURRENCY,
    BUDGET
}
