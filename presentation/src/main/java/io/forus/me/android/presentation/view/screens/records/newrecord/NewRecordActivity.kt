package io.forus.me.android.presentation.view.screens.records.newrecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

/**
 * Record creation screen
 */
class NewRecordActivity : CommonActivity() {


    companion object {



        fun getCallingIntent(context: Context ): Intent {
            val intent = Intent(context, NewRecordActivity::class.java)
            return intent
        }
    }

    lateinit var fragment : NewRecordFragment


    override val viewID: Int
        get() = R.layout.activity_toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {


            fragment = NewRecordFragment.newIntent()

            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onBackPressed() {
        if(fragment.onBackPressed())
            super.onBackPressed()
    }
}