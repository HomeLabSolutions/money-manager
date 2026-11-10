package com.d9tilov.android.notification

import com.d9tilov.android.notification.domain.MoneyManagerNotificationManager
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val notificationModule =
    module {
        factory<MoneyManagerNotificationManagerImpl>() bind MoneyManagerNotificationManager::class
    }
