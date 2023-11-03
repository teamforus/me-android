package io.forus.me.android.presentation.helpers

import android.R.attr.x
import java.security.MessageDigest
import java.text.NumberFormat
import java.util.*


/**
 * Created by pavel on 13.09.17.
 */

object Strings {

    fun isNullOrEmpty(str: String?): Boolean {
        return str == null || str.isEmpty()
    }

    fun capitalize(str: String): String? {
        if (isNullOrEmpty(str))
            return str
        return if (str.length == 0) str else str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()


    }

    fun toAppDoubleOrNull(source: String): Double {
        return source.toAppDoubleOrNull()
    }



}


fun String?.capitalize() : String {
    if (this == null)
        return ""

    return Strings.capitalize(this) ?: ""
}


fun String?.toAppFloat() : Float? {
    if (this == null)
        return null
    return this.toAppFloat()
}

fun String.toAppFloat() : Float {
    val myNumForm = NumberFormat.getInstance(Locale.getDefault())
    val myParsedFrenchNumber = myNumForm.parse(this).toFloat()
    return myParsedFrenchNumber
}


fun String?.toAppDoubleOrNull() : Double {
    val myNumForm = NumberFormat.getInstance(Locale.getDefault())
    val myParsedFrenchNumber = myNumForm.parse(this).toFloat().toDouble()
    return myParsedFrenchNumber
}

fun String.SHA256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}