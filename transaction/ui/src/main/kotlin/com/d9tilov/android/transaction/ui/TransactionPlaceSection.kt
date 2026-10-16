package com.d9tilov.android.transaction.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.transaction.domain.model.NearbyPlace
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.vm.NearbyPlacesStatus
import com.d9tilov.android.transaction.ui.vm.TransactionInfoUiState

@Composable
internal fun TransactionPlaceSection(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
    modifier: Modifier = Modifier,
) {
    if (uiState.transaction.type != TransactionType.EXPENSE) return
    val name = uiState.transaction.placeName
    if (uiState.mode == TransactionInfoMode.VIEW && name.isNullOrBlank()) return
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
    var customNameVisible by rememberSaveable { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text =
                uiState.transaction.placeName.orEmpty().ifBlank {
                    stringResource(R.string.transaction_place_not_selected)
                },
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        PlaceEditButton(
            loading = uiState.placeState.status == NearbyPlacesStatus.LOADING,
            onClick = actions.onEdit,
        )
    }
    if (uiState.placeState.dialogVisible) {
        PlaceSelectionDialog(
            uiState = uiState,
            actions = actions,
            onCustomName = {
                actions.onDismiss()
                customNameVisible = true
            },
        )
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
private fun PlaceSelectionDialog(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
    onCustomName: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = actions.onDismiss,
        text = {
            val itemColors = ListItemDefaults.colors(containerColor = Color.Transparent)
            LazyColumn(
                modifier = Modifier.heightIn(max = dimensionResource(R.dimen.transaction_places_dialog_max_height)),
            ) {
                item { PlaceListStatus(uiState) }
                item {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.transaction_place_custom_name)) },
                        modifier = Modifier.clickable(onClick = onCustomName),
                        colors = itemColors,
                    )
                }
                items(uiState.placeState.places, key = { it.id }) { place ->
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { PlaceDetails(place) },
                        modifier = Modifier.clickable { actions.onNameChanged(place.name) },
                        colors = itemColors,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = actions.onDismiss) {
                Text(stringResource(com.d9tilov.android.common.android.R.string.cancel))
            }
        },
    )
}

@Composable
private fun PlaceEditButton(
    loading: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, enabled = !loading) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.transaction_place_loader_size)),
                strokeWidth = dimensionResource(R.dimen.transaction_place_loader_stroke_width),
            )
        } else {
            Icon(
                imageVector = MoneyManagerIcons.Edit,
                contentDescription = stringResource(R.string.transaction_place_edit),
            )
        }
    }
}

@Composable
private fun PlaceListStatus(uiState: TransactionInfoUiState) {
    val message =
        when (uiState.placeState.status) {
            NearbyPlacesStatus.UNAVAILABLE -> {
                R.string.transaction_place_unavailable
            }

            NearbyPlacesStatus.NO_LOCATION -> {
                R.string.transaction_place_no_location
            }

            NearbyPlacesStatus.READY -> {
                if (uiState.placeState.places.isEmpty()) R.string.transaction_place_empty else null
            }

            else -> {
                null
            }
        }
    if (message != null) {
        Text(stringResource(message), style = MaterialTheme.typography.bodyMedium)
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
        if (place.address.isNotBlank()) {
            Text(
                place.address,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            stringResource(R.string.transaction_place_distance, place.distanceMeters),
            style = MaterialTheme.typography.bodySmall,
        )
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
