package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.d9tilov.android.core.utils.toLocalDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<LocalDate, LocalDate>) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val startDate = dateRangePickerState.selectedStartDateMillis?.toLocalDate(TimeZone.UTC)
            val endDate = dateRangePickerState.selectedEndDateMillis?.toLocalDate(TimeZone.UTC)
            TextButton(
                enabled = startDate != null && endDate != null,
                onClick = {
                    if (startDate != null && endDate != null) {
                        onDateRangeSelected(startDate to endDate)
                        onDismiss()
                    }
                },
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            showModeToggle = true,
            headline = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.date_range_picker_height))
                    .padding(
                        vertical = dimensionResource(R.dimen.padding_medium),
                        horizontal = dimensionResource(R.dimen.padding_extra_small),
                    ),
        )
    }
}

@Preview
@Composable
fun DateRangePickerModalPreview() {
    DateRangePickerModal(
        onDateRangeSelected = {},
        onDismiss = {},
    )
}
