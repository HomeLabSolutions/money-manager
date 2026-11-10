package com.d9tilov.moneymanager.di

import com.d9tilov.android.analytics.di.trackerDataModule
import com.d9tilov.android.backup.data.impl.di.backupDataImplComponents
import com.d9tilov.android.backup.di.backupDataModule
import com.d9tilov.android.backup.di.backupDomainModule
import com.d9tilov.android.billing.di.billingDataModule
import com.d9tilov.android.billing.di.billingDomainModule
import com.d9tilov.android.budget.di.budgetDataModule
import com.d9tilov.android.budget.di.budgetDomainModule
import com.d9tilov.android.budget.ui.di.budgetUiComponents
import com.d9tilov.android.category.di.categoryDataModule
import com.d9tilov.android.category.di.categoryDomainModule
import com.d9tilov.android.category.ui.vm.di.categoryUiComponents
import com.d9tilov.android.common.android.di.coroutinesModule
import com.d9tilov.android.common.android.di.locationModule
import com.d9tilov.android.currency.data.impl.sync.workers.di.currencyDataImplComponents
import com.d9tilov.android.currency.di.currencyApiModule
import com.d9tilov.android.currency.di.currencyDataModule
import com.d9tilov.android.currency.di.currencyDomainModule
import com.d9tilov.android.currency.ui.di.currencyUiComponents
import com.d9tilov.android.database.di.coreDatabaseComponents
import com.d9tilov.android.database.di.daosModule
import com.d9tilov.android.database.di.databaseModule
import com.d9tilov.android.datastore.di.datastoreModule
import com.d9tilov.android.incomeexpense.ui.vm.di.incomeexpenseUiComponents
import com.d9tilov.android.insights.di.insightsDataModule
import com.d9tilov.android.insights.di.insightsDomainModule
import com.d9tilov.android.insights.presentation.di.insightsPresentationComponents
import com.d9tilov.android.network.di.dispatchersModule
import com.d9tilov.android.network.di.networkModule
import com.d9tilov.android.notification.notificationModule
import com.d9tilov.android.places.di.placesApiModule
import com.d9tilov.android.places.di.placesDataModule
import com.d9tilov.android.places.di.placesDomainModule
import com.d9tilov.android.profile.ui.vm.di.profileUiComponents
import com.d9tilov.android.settings.ui.vm.di.settingsUiComponents
import com.d9tilov.android.statistics.ui.vm.di.statisticsUiComponents
import com.d9tilov.android.transaction.di.transactionDataModule
import com.d9tilov.android.transaction.di.transactionDomainModule
import com.d9tilov.android.transaction.regular.data.impl.notification.di.transactionRegularDataImplComponents
import com.d9tilov.android.transaction.regular.di.regularTransactionDataModule
import com.d9tilov.android.transaction.regular.di.regularTransactionDomainModule
import com.d9tilov.android.transaction.regular.ui.vm.di.transactionRegularUiComponents
import com.d9tilov.android.transaction.ui.vm.di.transactionUiComponents
import com.d9tilov.android.user.di.userDataModule
import com.d9tilov.android.user.di.userDomainModule
import com.d9tilov.android.widget.di.widgetDomainModule
import com.d9tilov.moneymanager.home.di.appComponents
import org.koin.dsl.module

val moneyManagerModule =
    module {
        includes(
            budgetDataModule,
            budgetDomainModule,
            billingDataModule,
            billingDomainModule,
            currencyDataModule,
            currencyDomainModule,
            backupDataModule,
            backupDomainModule,
            userDataModule,
            placesDomainModule,
            placesDataModule,
            notificationModule,
            categoryDataModule,
            categoryDomainModule,
            widgetDomainModule,
            regularTransactionDataModule,
            transactionDataModule,
            transactionDomainModule,
            insightsDataModule,
            insightsDomainModule,
            trackerDataModule,
            currencyApiModule,
            userDomainModule,
            placesApiModule,
            appModule,
            coroutinesModule,
            locationModule,
            datastoreModule,
            dispatchersModule,
            networkModule,
            daosModule,
            databaseModule,
            regularTransactionDomainModule,
            budgetUiComponents,
            currencyDataImplComponents,
            currencyUiComponents,
            backupDataImplComponents,
            profileUiComponents,
            incomeexpenseUiComponents,
            categoryUiComponents,
            appComponents,
            coreDatabaseComponents,
            settingsUiComponents,
            transactionRegularDataImplComponents,
            transactionRegularUiComponents,
            transactionUiComponents,
            insightsPresentationComponents,
            statisticsUiComponents,
        )
    }
