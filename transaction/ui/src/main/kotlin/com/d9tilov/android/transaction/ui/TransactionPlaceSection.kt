package com.d9tilov.android.transaction.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.transaction.domain.model.NearbyPlace
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.vm.TransactionInfoUiState

@Composable
internal fun TransactionPlaceSection(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
    modifier: Modifier = Modifier,
) {
    if (uiState.transaction.type != TransactionType.EXPENSE) return
    val name = uiState.transaction.placeName
    if (uiState.placeState.places.isEmpty() && name.isNullOrBlank()) return
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(
                dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small),
            ),
    ) {
        if (uiState.mode == TransactionInfoMode.EDIT) {
            TransactionPlaceNameRow(uiState, actions)
        } else {
            Text(stringResource(R.string.transaction_place_title), style = MaterialTheme.typography.labelLarge)
            Text(name.orEmpty(), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun TransactionPlaceNameRow(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var customNameVisible by rememberSaveable { mutableStateOf(false) }
    val places = uiState.placeState.places
    Box {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text =
                    uiState.transaction.placeName.orEmpty().ifBlank {
                        stringResource(
                            R.string.transaction_place_title,
                        )
                    },
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(onClick = {
                actions.onEdit()
                expanded = !expanded
            }) {
                Icon(
                    imageVector = MoneyManagerIcons.Edit,
                    contentDescription = stringResource(R.string.transaction_place_edit),
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = dimensionResource(R.dimen.transaction_places_menu_max_height)),
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.transaction_place_custom_name)) },
                onClick = {
                    expanded = false
                    customNameVisible = true
                },
            )
            places.forEach { place ->
                DropdownMenuItem(
                    text = { PlaceDetails(place) },
                    onClick = {
                        actions.onNameChanged(place.name)
                        expanded = false
                    },
                )
            }
        }
    }
    if (customNameVisible) {
        CustomPlaceNameDialog(
            name = uiState.transaction.placeName.orEmpty(),
            onNameChanged = actions.onNameChanged,
            onDismiss = { customNameVisible = false },
        )
    }
}

@Composable
private fun CustomPlaceNameDialog(
    name: String,
    onNameChanged: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var draft by rememberSaveable(name) { mutableStateOf(name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.transaction_place_name)) },
        text = {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onNameChanged(draft)
                onDismiss()
            }) { Text(stringResource(com.d9tilov.android.common.android.R.string.ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(com.d9tilov.android.common.android.R.string.cancel)) }
        },
    )
}

@Composable
private fun PlaceDetails(place: NearbyPlace) {
    Column {
        Text(place.name, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            stringResource(R.string.transaction_place_distance, place.distanceMeters),
            style = MaterialTheme.typography.bodySmall,
        )
        if (place.address.isNotBlank()) {
            Text(
                place.address,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun TransactionPlaceAttribution(
    uiState: TransactionInfoUiState,
    modifier: Modifier = Modifier,
) {
    if (uiState.transaction.type != TransactionType.EXPENSE) return
    if (uiState.placeState.places.isEmpty() && uiState.transaction.placeName.isNullOrBlank()) return
    val uriHandler = LocalUriHandler.current
    Row(modifier = modifier) {
        TextButton(onClick = { uriHandler.openUri("https://www.geoapify.com/") }) {
            Text(stringResource(R.string.transaction_place_geoapify), style = MaterialTheme.typography.labelSmall)
        }
        TextButton(onClick = { uriHandler.openUri("https://www.openstreetmap.org/copyright") }) {
            Text(stringResource(R.string.transaction_place_osm), style = MaterialTheme.typography.labelSmall)
        }
    }
}
