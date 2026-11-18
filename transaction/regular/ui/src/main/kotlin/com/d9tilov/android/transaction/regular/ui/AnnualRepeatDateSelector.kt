package com.d9tilov.android.transaction.regular.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toLocalDate
import com.d9tilov.android.core.utils.toMillis
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.time.MonthDay
import java.time.format.DateTimeFormatter

private val annualDateFormatter = DateTimeFormatter.ofPattern("dd.MM")

@Composable
internal fun AnnualRepeatDateSelector(
    period: ExecutionPeriod.EveryYear,
    onDateClicked: (LocalDate) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    Text(
        text = stringResource(R.string.regular_transaction_repeat_period_year, period.annualDateText()),
        modifier =
            Modifier
                .clickable { showDatePicker = true }
                .padding(vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.labelMedium,
    )
    if (showDatePicker) {
        AnnualDatePickerDialog(
            period = period,
            onDismiss = { showDatePicker = false },
            onDateClicked = onDateClicked,
        )
    }
}

internal fun ExecutionPeriod.EveryYear.annualDateText(): String =
    MonthDay.of(month, dayOfMonth).format(annualDateFormatter)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnnualDatePickerDialog(
    period: ExecutionPeriod.EveryYear,
    onDismiss: () -> Unit,
    onDateClicked: (LocalDate) -> Unit,
) {
    val initialDate =
        remember(period) {
            val monthDay = MonthDay.of(period.month, period.dayOfMonth)
            val year = generateSequence(currentDate().year) { it + 1 }.first { monthDay.isValidYear(it) }
            LocalDate(year, period.month, period.dayOfMonth)
        }
    val state =
        rememberDatePickerState(initialSelectedDateMillis = initialDate.getStartOfDay().toMillis(TimeZone.UTC))
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = state.selectedDateMillis != null,
                onClick = {
                    state.selectedDateMillis?.let { millis ->
                        onDateClicked(millis.toLocalDate(TimeZone.UTC))
                    }
                    onDismiss()
                },
            ) {
                Text(stringResource(com.d9tilov.android.common.android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(com.d9tilov.android.common.android.R.string.cancel))
            }
        },
    ) {
        DatePicker(state = state, title = {
            Text(
                text = stringResource(R.string.regular_transaction_repeat_year_date),
                modifier = Modifier.padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large)),
            )
        })
    }
}
