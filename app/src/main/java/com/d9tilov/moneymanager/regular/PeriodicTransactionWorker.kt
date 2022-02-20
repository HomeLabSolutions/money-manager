package com.d9tilov.moneymanager.regular

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.core.util.toMillis
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.ExecutionPeriod
import com.d9tilov.moneymanager.regular.domain.entity.PeriodType
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.domain.mapper.toCommon
import com.d9tilov.moneymanager.regular.ui.notification.TransactionNotificationBuilder
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PeriodicTransactionWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val notificationBuilder: TransactionNotificationBuilder
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.tag(App.TAG).d("Do regular transaction work ...")
        val output: Data = inputData
        val id = output.getLong(KEY_DATA, -1)
        return if (id == -1L) {
            Result.failure()
        } else {
            val regularTransaction = regularTransactionInteractor.getById(id)
            val sumInUsd = currencyInteractor.toUsd(regularTransaction.sum, regularTransaction.currencyCode)
            val commonTransaction = regularTransaction.toCommon(sumInUsd)
            if (regularTransaction.autoAdd) {
                transactionInteractor.addTransaction(commonTransaction)
            }
            if (regularTransaction.pushEnabled) {
                notificationBuilder.fireNotification(commonTransaction, regularTransaction.id)
            }
            Result.success()
        }
    }

    companion object {

        private const val KEY_DATA = "EXTRA_TRANSACTION_WORKER_DATA"
        private const val DELIMITER: String = "_"

        fun startPeriodicJob(
            context: Context,
            regularTransaction: RegularTransaction
        ) {
            Timber.tag(App.TAG).d("Start periodic transaction job")

            val listOfBuilders = mutableListOf<PeriodicWorkRequest.Builder>()
            when (regularTransaction.executionPeriod.periodType) {
                PeriodType.DAY -> {
                    val dailyBuilder = PeriodicWorkRequest.Builder(
                        PeriodicTransactionWorker::class.java,
                        24 * 3600 * 1000L, TimeUnit.MILLISECONDS
                    )
                    listOfBuilders.add(dailyBuilder)
                }
                PeriodType.WEEK -> {
                    val weekDays =
                        getDaysOfWeek((regularTransaction.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek)
                    for (day in weekDays) {
                        val weekBuilder =
                            PeriodicWorkRequest.Builder(
                                PeriodicTransactionWorker::class.java,
                                7 * 24 * 3600 * 1000L, TimeUnit.MILLISECONDS
                            ).setInitialDelay(
                                getInitialWeekDelayInMillis(day),
                                TimeUnit.MILLISECONDS
                            )
                        listOfBuilders.add(weekBuilder)
                    }
                }
                PeriodType.MONTH -> {
                    val monthlyBuilder = PeriodicWorkRequest.Builder(
                        PeriodicTransactionWorker::class.java,
                        30 * 24 * 3600 * 1000L, TimeUnit.MILLISECONDS
                    )
                        .setInitialDelay(
                            getInitialMonthDelayInMillis((regularTransaction.executionPeriod as ExecutionPeriod.EveryMonth).dayOfMonth),
                            TimeUnit.MILLISECONDS
                        )
                    listOfBuilders.add(monthlyBuilder)
                }
            }
            for (build in listOfBuilders) {
                val tag = createTag(regularTransaction)
                val work = build
                    .addTag(tag)
                    .setInputData(workDataOf(KEY_DATA to regularTransaction.id))
                    .build()
                val workManager = WorkManager.getInstance(context)
                workManager.enqueueUniquePeriodicWork(
                    PeriodicTransactionWorker::class.java.name,
                    ExistingPeriodicWorkPolicy.KEEP,
                    work
                )
            }
        }

        fun stopPeriodicJob(
            context: Context,
            regularTransaction: RegularTransaction
        ) {
            Timber.tag(App.TAG).d("Stop transaction periodic job")
            WorkManager.getInstance(context)
                .cancelAllWorkByTag(createTag(regularTransaction))
        }

        private fun getInitialWeekDelayInMillis(dayOfWeek: Int): Long {
            val c = Calendar.getInstance()
            val currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK)
            return when {
                dayOfWeek == currentDayOfWeek -> 0
                currentDayOfWeek < dayOfWeek -> (dayOfWeek - currentDayOfWeek) * 24 * 3600 * 1000L
                else -> {
                    val daysInWeek = 7
                    (dayOfWeek + daysInWeek - currentDayOfWeek) * 24 * 3600 * 1000L
                }
            }
        }

        private fun getInitialMonthDelayInMillis(dayOfMonth: Int): Long {
            val c = Calendar.getInstance()
            val currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH)
            return when {
                dayOfMonth == currentDayOfMonth -> 0
                currentDayOfMonth < dayOfMonth -> (dayOfMonth - currentDayOfMonth) * 24 * 3600 * 1000L
                else -> {
                    val daysInCurrentMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
                    (dayOfMonth + daysInCurrentMonth - currentDayOfMonth) * 24 * 3600 * 1000L
                }
            }
        }

        private fun getInitialYearDelayInMillis(dayOfYear: Int): Long {
            val c = Calendar.getInstance()
            val currentDayOfYear = c.get(Calendar.DAY_OF_YEAR)
            return when {
                dayOfYear == currentDayOfYear -> 0
                currentDayOfYear < dayOfYear -> (dayOfYear - currentDayOfYear) * 24 * 3600 * 1000L
                else -> {
                    val daysInCurrentYear = c.getActualMaximum(Calendar.DAY_OF_YEAR)
                    (dayOfYear + daysInCurrentYear - currentDayOfYear) * 24 * 3600 * 1000L
                }
            }
        }

        private fun getDaysOfWeek(days: Int): List<Int> {
            val weekDays = mutableListOf<Int>()
            for (i in 0..6) {
                if ((days shr i) and 1 == 1) {
                    weekDays.add(i)
                }
            }
            return weekDays
        }

        private fun createTag(regularTransaction: RegularTransaction): String =
            regularTransaction.id.toString() + DELIMITER + regularTransaction.executionPeriod.periodType.name + DELIMITER + regularTransaction.executionPeriod.periodType + DELIMITER + regularTransaction.executionPeriod.lastExecutionDateTime.toMillis() + DELIMITER
    }
}
