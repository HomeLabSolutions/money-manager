package com.d9tilov.android.notification

import com.d9tilov.android.notification.domain.MoneyManagerNotificationManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface NotificationModule {
    @Binds
    val MoneyManagerNotificationManagerImpl.bindManager: MoneyManagerNotificationManager<AndroidNotificationData>
}
