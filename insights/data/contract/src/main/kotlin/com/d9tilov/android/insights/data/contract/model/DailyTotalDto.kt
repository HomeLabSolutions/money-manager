package com.d9tilov.android.insights.data.contract.model

data class DailyTotalDto(
    val date: String,
    val type: String,
    val currency: String,
    val amount: String,
) {
    fun toPayload(): Map<String, String> =
        mapOf(
            "date" to date,
            "type" to type,
            "currency" to currency,
            "amount" to amount,
        )
}
