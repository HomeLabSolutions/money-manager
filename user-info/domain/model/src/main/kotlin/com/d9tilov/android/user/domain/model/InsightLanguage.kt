package com.d9tilov.android.user.domain.model

enum class InsightLanguage(
    val tag: String,
) {
    SYSTEM("system"),
    ENGLISH("en"),
    RUSSIAN("ru"),
    SPANISH("es"),
    PORTUGUESE("pt"),
    ARABIC("ar"),
    HINDI("hi"),
    CHINESE("zh-CN"),
    ;

    companion object {
        val supportedLanguages: List<InsightLanguage> = entries.toList()

        fun fromTag(tag: String?): InsightLanguage = supportedLanguages.firstOrNull { it.tag == tag } ?: SYSTEM
    }
}
