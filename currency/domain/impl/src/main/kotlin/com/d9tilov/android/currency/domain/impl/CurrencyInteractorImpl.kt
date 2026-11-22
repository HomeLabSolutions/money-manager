package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.mapper.toDomain
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import com.d9tilov.android.currency.domain.model.DomainCurrency
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

@Inject
class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
) : CurrencyInteractor {
    override fun getCurrencies(): Flow<List<DomainCurrency>> =
        currencyRepo
            .getMainCurrency()
            .flatMapMerge { baseCurrency ->
                currencyRepo
                    .getCurrencies()
                    .map { list -> list.map { item -> item.toDomain(baseCurrency.code == item.code) } }
            }

    override fun getMainCurrencyFlow(): Flow<CurrencyMetaData> = currencyRepo.getMainCurrency()

    override suspend fun getMainCurrency(): CurrencyMetaData =
        currencyRepo.getMainCurrency().firstOrNull() ?: CurrencyMetaData.EMPTY

    override suspend fun getCurrencyByCode(code: String): CurrencyRate = currencyRepo.getCurrencyByCode(code)

    override suspend fun toTargetCurrency(
        amount: Decimal,
        sourceCurrencyCode: String,
        targetCurrencyCode: String,
    ): Decimal {
        if (sourceCurrencyCode == targetCurrencyCode) return amount
        val targetCurrency = getCurrencyByCode(targetCurrencyCode)
        val sourceCurrency = getCurrencyByCode(sourceCurrencyCode)
        val precision = Currency.valueOf(targetCurrencyCode).precision
        return (amount * targetCurrency.value).divide(sourceCurrency.value, precision)
    }

    override suspend fun toUsd(
        amount: Decimal,
        currencyCode: String,
    ): Decimal {
        val usd = Currency.USD
        if (currencyCode == usd.name) return amount.roundTo(usd.precision)
        return amount.divide(getCurrencyByCode(currencyCode).value, usd.precision)
    }

    override suspend fun updateCurrencyRates(): Result<Boolean> =
        try {
            currencyRepo.updateCurrencies()
            Result.success(true)
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            Result.failure(ex)
        }

    override suspend fun updateMainCurrency(code: String) = currencyRepo.updateMainCurrency(code)
}
