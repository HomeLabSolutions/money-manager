package com.d9tilov.moneymanager.widget

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.delegatedData
import java.util.concurrent.TimeUnit

object SpendingWidgetWork {
    private const val WORK_NAME = "spending_widget_update"
    private const val PERIODIC_WORK_NAME = "spending_widget_periodic_update"
    private const val UPDATE_INTERVAL_MINUTES = 30L

    fun schedule(context: Context) {
        if (!SpendingWidgetProvider.hasWidgets(context)) return
        SpendingWidgetObserver.get(context).start()
        val request =
            PeriodicWorkRequestBuilder<DelegatingWorker>(UPDATE_INTERVAL_MINUTES, TimeUnit.MINUTES)
                .setInitialDelay(UPDATE_INTERVAL_MINUTES, TimeUnit.MINUTES)
                .setInputData(SpendingWidgetWorker::class.delegatedData())
                .addTag(WORK_NAME)
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
        refresh(context)
    }

    fun refresh(context: Context) {
        if (!SpendingWidgetProvider.hasWidgets(context)) return
        val request =
            OneTimeWorkRequestBuilder<DelegatingWorker>()
                .setInputData(SpendingWidgetWorker::class.delegatedData())
                .addTag(WORK_NAME)
                .build()
        WorkManager.getInstance(context).enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancel(context: Context) {
        SpendingWidgetObserver.get(context).stop()
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_NAME)
    }
}
