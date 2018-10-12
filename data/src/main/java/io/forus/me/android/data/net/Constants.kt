package io.forus.me.android.data.net


import io.forus.me.android.data.BuildConfig

object Constants {


    val BASE_SERVICE_ENDPOINT = if (BuildConfig.DEBUG) "https://demo.api.forus.link/" else "https://api.forus.link/"
}
