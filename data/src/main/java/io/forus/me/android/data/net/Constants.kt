package io.forus.me.android.data.net


import io.forus.me.android.data.BuildConfig
public object Constants {


    val BASE_SERVICE_ENDPOINT = if (BuildConfig.DEBUG) "https://test.platform.forus.io/" else "https://test.platform.forus.io/"
}