package io.forus.me.android.presentation.view.screens.account.account.check_email


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.text.HtmlCompat
import android.text.Html


import io.forus.me.android.presentation.view.activity.CommonActivity

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SharedPref
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




        SharedPref.init(this@CheckEmailActivity)
        val restoreEmail = SharedPref.read(SharedPref.RESTORE_EMAIL, "")

        val descriptionText = getString(R.string.check_email_description_part1) + " <b><i><font color=\"blue\">" + restoreEmail + "</font></i></b> " + getString(R.string.check_email_description_part2)
        description.text = HtmlCompat.fromHtml(descriptionText, HtmlCompat.FROM_HTML_MODE_LEGACY);


        back.setOnClickListener { finish() }
        pair_device.setOnClickListener { navigator.navigateToPairDevice(this@CheckEmailActivity) }

        checkEmailBt.setOnClickListener {

            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            startActivity(intent)
            startActivity(Intent.createChooser(intent, getString(R.string.check_email_open_mail_app)))
        }
    }
}
