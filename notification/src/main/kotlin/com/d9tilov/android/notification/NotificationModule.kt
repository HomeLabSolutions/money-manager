package com.d9tilov.android.notification

import com.d9tilov.android.notification.domain.MoneyManagerNotificationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NotificationModule {
    @Binds
    fun bindNotificationManager(
        impl: MoneyManagerNotificationManagerImpl,
    ): MoneyManagerNotificationManager<AndroidNotificationData>
}
