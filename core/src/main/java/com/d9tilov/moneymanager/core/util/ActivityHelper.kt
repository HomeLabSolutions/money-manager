@file:JvmName("ActivityHelper")

package com.d9tilov.moneymanager.core.util

import android.content.Intent

/**
 * Helpers to start activities in a modularized world.
 */

private const val PACKAGE_NAME = "com.d9tilov.moneymanager"

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        PACKAGE_NAME,
        addressableActivity.className
    )
}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

/**
 * All addressable activities.
 *
 * Can contain intent extra names or functions associated with the activity creation.
 */
object Activities {

    /**
     * AuthActivity
     */
    object Auth : AddressableActivity {
        override val className = "$PACKAGE_NAME.auth.ui.AuthActivity"
    }

    object Home : AddressableActivity {
        override val className = "$PACKAGE_NAME.home"
    }
}
