package io.forus.me.android.presentation.helpers

import android.content.Context
import android.util.DisplayMetrics
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


object Converter {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return Math.round(dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT) * 100) / 100
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertBigDecimalToStringNL(currency: BigDecimal): String {
        var out = NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                .format(currency)
        out = out.replace(".00", ",-")
        out = out.replace(",00", ",-")
        return out
    }

    fun convertBigDecimalToDiscountString(discount: BigDecimal): String {

        var out = discount.toPlainString()// = NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                //.format(discount)
        if((discount.toDouble() - discount.toDouble().roundToInt()) == 0.0){
            out = out.replace(".00", "%")
            out = out.replace(",00", "%")
        }else{
            out += "%"
        }

        return out
    }
}