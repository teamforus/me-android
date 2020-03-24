package io.forus.me.android.presentation.view.screens.account.account.check_email


import android.content.Context
import android.content.Intent
import android.os.Bundle


import io.forus.me.android.presentation.view.activity.CommonActivity

import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.activity_check_email.*


/**
 * Main application screen. This is the app entry point.
 */
class CheckEmailActivity : CommonActivity() {


    companion object {


        fun getCallingIntent(context: Context): Intent {
            return Intent(context, CheckEmailActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_check_email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, CheckEmailFragment())
        }*/

        back.setOnClickListener { finish() }
        cancelBt.setOnClickListener { finish() }

        checkEmailBt.setOnClickListener {

            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            startActivity(intent)
            startActivity(Intent.createChooser(intent, getString(R.string.check_email_open_mail_app)))
        }
    }
}
