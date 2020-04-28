package io.forus.me.android.presentation.view.screens.account.pair_device


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.activity.CommonActivity

/**
 * Activity Pair Device.
 */
class PairDeviceActivity : CommonActivity() {


    companion object {


        fun getCallingIntent(context: Context): Intent {
            return Intent(context, PairDeviceActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, PairDeviceFragment())
        }
    }
}
