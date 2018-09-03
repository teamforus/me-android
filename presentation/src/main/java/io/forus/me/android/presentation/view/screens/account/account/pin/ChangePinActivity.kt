package io.forus.me.android.presentation.view.screens.account.account.pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

class ChangePinActivity : CommonActivity() {


    companion object {

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, ChangePinActivity::class.java)
            return intent
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = ChangePinFragment.newIntent()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }
}