package io.forus.me.android.presentation.helpers

import java.text.NumberFormat
import java.util.*

object NumberUtils {




    fun roundFloatToTwoDigits(number: Float): Float {

        var pow = 10
        for (i in 1..1)
            pow *= 10
        val tmp = number * pow
        return (if (tmp - tmp.toInt() >= 0.5f) tmp + 1 else tmp).toInt().toFloat() / pow

    }

    fun format(source: Double): String {
        return source.format()
    }



}

fun Double?.format(digits: Int = 6) = (if (this == null) "" else java.lang.String.format(Locale.US, "%.${digits}f", this))!!

fun Float?.format(digits: Int = 6) = this?.toDouble().format(digits)



