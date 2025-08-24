package com.d9tilov.android.analytics.model

sealed class AnalyticsEvent(
    val name: String,
) {
    sealed class Internal(
        name: String,
    ) : AnalyticsEvent(name) {
        sealed class Screen(
            name: String,
        ) : Internal("screen_$name") {
            data object Main : Screen("main")

            sealed class Category(
                name: String,
            ) : Screen("category_$name") {
                data object List : Category("list")

                data object Details : Category("details")

                data object IconGrid : Category("icon_grid")

                data object GroupIconList : Category("group_icon_list")

                data object Color : Category("color")
            }

            sealed class Statistics(
                name: String,
            ) : Screen("statistics_$name") {
                data object Parent : Statistics("parent")

                data object Details : Statistics("details")
            }

            sealed class Profile(
                name: String,
            ) : Screen("profile_$name") {
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
        ) : Internal("backup_$name") {
            data object Restored : Backup("restored")

            data object Saved : Backup("saved")

            data object Delete : Backup("deleted")

            data object Error : Backup("error")
        }

        sealed class Error(
            name: String,
        ) : Internal(name) {
            data object WrongUidException : Error("wrong_uid_exception")

            data object NetworkException : Error("network_issue")

            data object FileNotFoundException : Error("file_not_found_exception")

            data object FirebaseException : Error("firebase_exception")
        }
    }

    sealed class Client(
        name: String,
    ) : AnalyticsEvent(name) {
        sealed class Auth(
            name: String,
        ) : Client("auth_$name") {
            data object Login : Auth("login")

            data object Logout : Auth("logout")
        }
    }
}
