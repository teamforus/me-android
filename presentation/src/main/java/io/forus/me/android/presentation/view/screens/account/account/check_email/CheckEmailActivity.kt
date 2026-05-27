package io.forus.me.android.presentation.view.screens.account.account.check_email


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.core.text.HtmlCompat


import io.forus.me.android.presentation.view.activity.CommonActivity

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityCheckEmailBinding


/**
 * Main application screen. This is the app entry point.
 */
class CheckEmailActivity : CommonActivity() {


    companion object {

        private const val EMAIL_EXTRA = "EMAIL_EXTRA"

        fun getCallingIntent(context: Context, email: String? = null): Intent {
            return Intent(context, CheckEmailActivity::class.java).also {
                if (!email.isNullOrBlank()) {
                    it.putExtra(EMAIL_EXTRA, email)
                }
            }
        }
    }

    override val viewID: Int
        get() = R.layout.activity_check_email

    private lateinit var binding: ActivityCheckEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra(EMAIL_EXTRA)

        binding.description.text = if (email.isNullOrBlank()) {
            getString(R.string.check_email_description)
        } else {
            HtmlCompat.fromHtml(
                getString(R.string.check_email_description_with_email, TextUtils.htmlEncode(email)),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }


        binding.back.setOnClickListener { finish() }
        binding.pairDevice.setOnClickListener { navigator.navigateToPairDevice(this@CheckEmailActivity) }

        binding.checkEmailBt.setOnClickListener {

            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            startActivity(intent)
            startActivity(Intent.createChooser(intent, getString(R.string.check_email_open_mail_app)))
        }
    }
}
