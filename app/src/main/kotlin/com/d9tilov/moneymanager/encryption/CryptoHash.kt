package com.d9tilov.moneymanager.encryption

import android.util.Base64
import java.security.MessageDigest
import java.text.Normalizer

fun digest(vararg data: String?): String {
    val srcString = arrayOf(*data)
        .map { s -> s ?: "" }
        .map { s: String -> s.trim() }
        .map { s: String -> Normalizer.normalize(s, Normalizer.Form.NFC) }
        .joinToString { "|" }
    val srcBytes = srcString.toByteArray()

    val md = MessageDigest.getInstance("SHA-256")
    val result = md.digest(srcBytes)
    return Base64.encodeToString(result, Base64.DEFAULT)
}

fun String?.equalsDigest(hashDigest: String?): Boolean = hashDigest == digest(this)
