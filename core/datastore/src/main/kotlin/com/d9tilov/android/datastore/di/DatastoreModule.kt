package com.d9tilov.android.datastore.di

import android.content.Context
import com.d9tilov.android.datastore.PreferencesStore
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun providePreferenceStore(context: Context): PreferencesStore = PreferencesStore(context)

val datastoreModule =
    module {
        single { create(::providePreferenceStore) }
    }
