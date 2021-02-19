package io.forus.me.android.presentation.helpers

import android.content.Context
import android.content.SharedPreferences
import android.app.Activity


object SharedPref {
    private var mSharedPref: SharedPreferences? = null

    const val RESTORE_EMAIL = "RESTORE_EMAIL"
    const val OPTION_SEND_CRASH_REPORT = "SEND_CRASH_REPORT"
    const val OPTION_NEED_APP_UPDATE = "OPTION_NEED_APP_UPDATE"
    const val OPTION_API_TYPE = "OPTION_API_TYPE"
    const val OPTION_CUSTOM_API_URL = "OPTION_CUSTOM_API_URL"
    const val OPTION_SHOW_TOOLTIP_ADD_RECORD = "OPTION_SHOW_TOOLTIP_ADD_RECORD"
    const val IS_MUST_SHOW_ONBOARD_SCREENS = "IS_MUST_SHOW_ONBOARD_SCREENS"

    const val APP_REVIEW_LAST_BUILD_VERSION = "APP_REVIEW_LAST_BUILD_VERSION"
    const val APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES = "APP_REVIEW_TOTAL_COUNT_APP_LAUNCHES"
    const val APP_REVIEW_COUNT_REQUESTS = "APP_REVIEW_COUNT_REQUESTS"
    const val APP_REVIEW_LAST_REQUEST_TIME = "APP_REVIEW_LAST_REQUEST_TIME"

    fun init(context: Context) {
        if (mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    fun read(key: String, defValue: String): String? {
        return mSharedPref!!.getString(key, defValue)
    }

    fun write(key: String, value: String) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun read(key: String, defValue: Boolean): Boolean {
        return mSharedPref!!.getBoolean(key, defValue)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.commit()
    }

    fun read(key: String, defValue: Int): Int {
        return mSharedPref!!.getInt(key, defValue)
    }

    fun write(key: String, value: Int?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putInt(key, value!!).commit()
    }
}



