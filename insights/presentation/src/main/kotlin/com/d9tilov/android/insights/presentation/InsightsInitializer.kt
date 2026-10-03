package com.d9tilov.android.insights.presentation

import android.content.Context
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import com.d9tilov.android.insights.presentation.worker.WeeklyInsightWorker
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class InsightsInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val consentRepository: InsightsConsentRepository,
    private val applicationScope: CoroutineScope,
) {
    fun initialize() {
        applicationScope.launch(Dispatchers.IO) {
            consentRepository.isGranted.distinctUntilChanged().collect { granted ->
                if (granted) {
                    WeeklyInsightWorker.startPeriodicJob(context)
                } else {
                    WeeklyInsightWorker.stopPeriodicJob(context)
                }
            }
        }
    }

    companion object {
        fun initialize(context: Context) {
            EntryPointAccessors
                .fromApplication<InsightsInitializerEntryPoint>(context.applicationContext)
                .insightsInitializer()
                .initialize()
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InsightsInitializerEntryPoint {
    fun insightsInitializer(): InsightsInitializer
}
