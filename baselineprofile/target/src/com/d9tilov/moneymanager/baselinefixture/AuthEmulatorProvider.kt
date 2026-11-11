package com.d9tilov.moneymanager.baselinefixture

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth

private const val AUTH_EMULATOR_PORT = 9099

class AuthEmulatorProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        check(requireNotNull(context).packageName.endsWith(".baselineprofile"))
        FirebaseAuth.getInstance().useEmulator("127.0.0.1", AUTH_EMULATOR_PORT)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? = null

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0
}
