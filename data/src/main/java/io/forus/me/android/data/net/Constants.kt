package io.forus.me.android.data.net


import io.forus.me.android.data.BuildConfig

object Constants {


    val BASE_SERVICE_ENDPOINT = if (BuildConfig.DEBUG) "https://dev.api.forus.link/" else "https://dev.api.forus.link/"
}