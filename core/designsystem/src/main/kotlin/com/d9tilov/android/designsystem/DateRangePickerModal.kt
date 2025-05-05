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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long, Long>) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis ?: currentDateTime().toMillis(),
                            dateRangePickerState.selectedEndDateMillis ?: currentDateTime().toMillis(),
                        ),
                    )
                    onDismiss()
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
                    .height(500.dp)
                    .padding(vertical = 16.dp, horizontal = 4.dp),
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
