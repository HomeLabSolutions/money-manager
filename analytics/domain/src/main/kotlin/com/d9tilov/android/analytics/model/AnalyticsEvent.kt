package com.d9tilov.android.analytics.model

sealed class AnalyticsEvent(
    val name: String,
) {
    sealed class Internal(
        name: String,
    ) : AnalyticsEvent(name) {
        sealed class Backup(
            name: String,
        ) : Internal(name) {
            data object Restored : Backup("backup_restored")

            data object Saved : Backup("backup_saved")

            data object Delete : Backup("backup_saved")

            data object Error : Backup("backup_error")
        }

        sealed class Error(
            name: String,
        ) : Internal(name) {
            data object WrongUidException : Error("Wrong uid exception")

            data object NetworkException : Error("Network issue")

            data object FileNotFoundException : Error("File not found exception")

            data object FirebaseException : Error("Firebase exception")
        }
    }

    sealed class Client(
        name: String,
    ) : AnalyticsEvent(name) {
        sealed class Auth(
            name: String,
        ) : Client(name) {
            data object Login : Auth("login")

            data object Logout : Auth("logout")
        }
    }
}
