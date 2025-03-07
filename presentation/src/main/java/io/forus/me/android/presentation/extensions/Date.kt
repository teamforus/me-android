package io.forus.me.android.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date?.formatToDisplay(): String {
    if (this == null) return "N/A" // Handle null case with a default value
    val formatter = SimpleDateFormat("HH:mm - dd MMM, yyyy", Locale.getDefault())
    return formatter.format(this)
}