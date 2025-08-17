package com.d9tilov.android.analytics.model

sealed class AnalyticsEvent(
    val name: String,
) {
    sealed class Internal(
        name: String,
    ) : AnalyticsEvent(name) {
        sealed class Screen(
            name: String,
        ) : Internal(name) {
            data object Main : Screen("main")

            sealed class Statistics(
                name: String,
            ) : Screen("statistics.$name") {
                data object Parent : Statistics("parent")

                data object Details : Statistics("details")
            }

            sealed class Profile(
                name: String,
            ) : Screen("profile.$name") {
                data object Parent : Profile("parent")

                data object CurrencyList : Profile("currency_list")

                data object Budget : Profile("budget")

                data object RegularIncomes : Profile("regular_incomes")

                data object RegularExpenses : Profile("regular_expenses")

                data object Settings : Profile("settings")
            }
        }

        sealed class Backup(
            name: String,
        ) : Internal(name) {
            data object Restored : Backup("backup_restored")

            data object Saved : Backup("backup_saved")

            data object Delete : Backup("backup_deleted")

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
