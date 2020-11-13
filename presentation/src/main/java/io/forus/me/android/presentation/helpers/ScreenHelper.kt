package io.forus.me.android.presentation.helpers

import android.app.Activity
import android.util.DisplayMetrics

class ScreenHelper{

    companion object {
        fun isSmallScreen(activity: Activity): Boolean {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val density = activity.resources.displayMetrics.density
            val dpHeight = outMetrics.heightPixels / density
            val dpWidth = outMetrics.widthPixels / density
            return dpWidth <= 320 || dpHeight < 522
        }

    }
}