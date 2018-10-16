package io.forus.me.android.presentation.view.activity

import android.os.Bundle
import android.util.Log
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.deeplinks.UriToIntentMapper

class LinkDispatcherActivity : BaseActivity() {
    private val mMapper = UriToIntentMapper(this, navigator)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mMapper.dispatchIntent(intent)

        } catch (iae: IllegalArgumentException) {
            // Malformed URL
            if (BuildConfig.DEBUG) {
                Log.e("Deep links", "Invalid URI", iae)
            }
        } finally {
            // Always finish the Activity so that it doesn't stay in our history
            finish()
        }
    }
}