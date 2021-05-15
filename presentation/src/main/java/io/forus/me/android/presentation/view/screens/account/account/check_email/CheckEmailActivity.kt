package io.forus.me.android.presentation.view.screens.account.account.check_email


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.text.HtmlCompat
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log


import io.forus.me.android.presentation.view.activity.CommonActivity

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.view.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_check_email.*


/**
 * Main application screen. This is the app entry point.
 */
class CheckEmailActivity :  BaseActivity() {


    companion object {


        fun getCallingIntent(context: Context): Intent {
            return Intent(context, CheckEmailActivity::class.java)
        }
    }




    fun checkIsSmallScreen(): Boolean {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density
        //R.layout.activity_check_email
        Log.d("forus","scrW=$dpWidth  scrH=$dpHeight")

       return dpWidth <= 320 || dpHeight < 522
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutId = if (checkIsSmallScreen())
            R.layout.activity_check_email_nokia1
        else R.layout.activity_check_email
        setContentView(layoutId)



        SharedPref.init(this@CheckEmailActivity)
        val restoreEmail = SharedPref.read(SharedPref.RESTORE_EMAIL, "")

        val descriptionText = getString(R.string.check_email_description_part1) + " <b><i><font color=\"blue\">" + restoreEmail + "</font></i></b> " + getString(R.string.check_email_description_part2)
        description.text = HtmlCompat.fromHtml(descriptionText, HtmlCompat.FROM_HTML_MODE_LEGACY)


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

