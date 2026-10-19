package com.d9tilov.android.incomeexpense.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo
import com.d9tilov.android.incomeexpense.ui.vm.IncomeInfo
import com.d9tilov.android.incomeexpense.ui.vm.Price

@Composable
fun ExpenseInfoBlock(
    modifier: Modifier,
    info: ExpenseInfo,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        InfoLabel(info.ableToSpendToday)
        InfoLabel(info.wasSpendToday)
        InfoLabel(info.wasSpendInPeriod)
    }
}

@Composable
fun IncomeInfoBlock(
    modifier: Modifier,
    info: IncomeInfo,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        InfoLabel(info.wasEarnedInPeriod)
    }
}

@Composable
fun InfoLabel(price: Price) {
    Row(
        Modifier.padding(bottom = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_extra_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfoLabelTitle(text = stringResource(id = price.label))
        Text(
            modifier =
                Modifier.padding(
                    start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_extra_small),
                ),
            text = price.value,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
fun InfoLabelTitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier =
            modifier
                .clip(
                    RoundedCornerShape(
                        dimensionResource(com.d9tilov.android.designsystem.R.dimen.income_expense_badge_corner_radius),
                    ),
                ).background(MaterialTheme.colorScheme.primary),
    ) {
        Text(
            modifier = Modifier.padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_compact)),
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
        )
    }
}
