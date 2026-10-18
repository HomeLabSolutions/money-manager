package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.R
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.navigation.TransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionInfoUiState(
    val mode: TransactionInfoMode = TransactionInfoMode.VIEW,
    val amount: String = "0",
    val transaction: Transaction =
        Transaction.EMPTY.copy(
            category =
                Category.EMPTY_EXPENSE.copy(
                    color = android.R.color.transparent,
                    icon = R.drawable.dummy_icon,
                ),
        ),
    val placeState: TransactionPlaceState = TransactionPlaceState(),
) {
    companion object {
        val EMPTY = TransactionInfoUiState()
    }
}

@HiltViewModel
class TransactionInfoViewModel
    @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val transactionInteractor: TransactionInteractor,
        private val categoryInteractor: CategoryInteractor,
        private val nearbyPlacesInteractor: NearbyPlacesInteractor,
    ) : ViewModel() {
        private val transactionArgs: TransactionArgs.TransactionInfoArgs =
            TransactionArgs.TransactionInfoArgs(savedStateHandle)
        private val _uiState = MutableStateFlow(TransactionInfoUiState.EMPTY)
        val uiState = _uiState.asStateFlow()
        private val transactionId: Long = transactionArgs.transactionId

        init {
            val transactionExceptionHandler = CoroutineExceptionHandler { _, _ -> }
            viewModelScope.launch(transactionExceptionHandler) {
                val transactions = transactionInteractor.getTransactionById(transactionId)
                val loadedTransactions =
                    if (transactionArgs.mode == TransactionInfoMode.EDIT) transactions.take(1) else transactions
                loadedTransactions.collect { transaction ->
                    _uiState.update { state ->
                        state.copy(
                            mode = transactionArgs.mode,
                            amount = transaction.sum.reduceScaleStr(),
                            transaction = transaction,
                        )
                    }
                }
            }
        }

        fun updateAmount(amount: String) = _uiState.update { state -> state.copy(amount = amount) }

        fun updatePlaceName(
            name: String,
            address: String? = null,
        ) {
            if (!canEditPlace()) return
            _uiState.update { state ->
                state.copy(
                    transaction =
                        state.transaction.copy(
                            placeName = name,
                            placeAddress = address?.takeIf { name.isNotBlank() && it.isNotBlank() },
                        ),
                    placeState = state.placeState.copy(pickerVisible = false),
                )
            }
        }

        fun loadNearbyPlaces() {
            if (!canEditPlace()) return
            val state = _uiState.value
            if (state.placeState.status == NearbyPlacesStatus.LOADING) return
            if (state.placeState.status == NearbyPlacesStatus.READY) {
                _uiState.update { it.copy(placeState = it.placeState.copy(pickerVisible = true)) }
                return
            }
            val location = state.transaction.locationData
            if (!hasUsableCoordinates(location)) {
                _uiState.update {
                    it.copy(
                        placeState = it.placeState.copy(status = NearbyPlacesStatus.NO_LOCATION, pickerVisible = true),
                    )
                }
                return
            }
            _uiState.update {
                it.copy(
                    placeState =
                        it.placeState.copy(
                            status = NearbyPlacesStatus.LOADING,
                            places = emptyList(),
                            pickerVisible = false,
                        ),
                )
            }
            viewModelScope.launch {
                try {
                    val places = nearbyPlacesInteractor.getNearbyPlaces(location)
                    _uiState.update {
                        it.copy(
                            placeState =
                                it.placeState.copy(
                                    places = places,
                                    status = NearbyPlacesStatus.READY,
                                    pickerVisible = true,
                                ),
                        )
                    }
                } catch (error: CancellationException) {
                    throw error
                } catch (_: Exception) {
                    _uiState.update {
                        it.copy(
                            placeState =
                                it.placeState.copy(
                                    status = NearbyPlacesStatus.UNAVAILABLE,
                                    pickerVisible = true,
                                ),
                        )
                    }
                }
            }
        }

        fun dismissPlacePicker() {
            _uiState.update { state -> state.copy(placeState = state.placeState.copy(pickerVisible = false)) }
        }

        private fun canEditPlace(): Boolean =
            _uiState.value.run { mode == TransactionInfoMode.EDIT && transaction.type == TransactionType.EXPENSE }

        private fun hasUsableCoordinates(location: LocationData): Boolean {
            val validLatitude = location.latitude.isFinite() && location.latitude in MIN_LATITUDE..MAX_LATITUDE
            val validLongitude = location.longitude.isFinite() && location.longitude in MIN_LONGITUDE..MAX_LONGITUDE
            return location != LocationData.EMPTY && validLatitude && validLongitude
        }

        fun updateDescription(description: String) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(description = description))
            }

        fun updateDate(date: Long) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(date = date.toLocalDateTime()))
            }

        fun updateInStatistics(inStatistics: Boolean) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(inStatistics = inStatistics))
            }

        fun updateCurrencyCode(code: String) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(currencyCode = code))
            }

        fun updateCategory(id: Long) =
            viewModelScope.launch {
                val category = categoryInteractor.getCategoryById(id)
                _uiState.update { state ->
                    val tr = state.transaction
                    state.copy(transaction = tr.copy(category = category))
                }
            }

        suspend fun save() {
            val state = _uiState.value
            if (state.mode == TransactionInfoMode.VIEW) return
            transactionInteractor.update(
                state.transaction.copy(
                    sum = state.amount.toBigDecimal(),
                    placeName = state.transaction.placeName?.trim(),
                    placeAddress = state.transaction.placeAddress?.trim(),
                ),
            )
        }

        private companion object {
            const val MIN_LATITUDE = -90.0
            const val MAX_LATITUDE = 90.0
            const val MIN_LONGITUDE = -180.0
            const val MAX_LONGITUDE = 180.0
        }
    }
