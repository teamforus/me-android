package io.forus.me.android.presentation.view.screens.account.login_signup_account


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity


/**
 * Main application screen. This is the app entry point.
 */
class LogInSignUpActivity : CommonActivity() {


    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun getCallingIntent(context: Context, token: String): Intent {
            val intent = Intent(context, LogInSignUpActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            return intent
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, LogInSignUpActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    private lateinit var fragment: LogInSignUpFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val token = intent.getStringExtra(TOKEN_EXTRA)
            fragment = if(token != null){
                LogInSignUpFragment.newIntent(token)
            } else LogInSignUpFragment()
            val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)
            fragmentContainer.setLayoutParams(RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        fragment.exchangeToken(intent.getStringExtra(TOKEN_EXTRA))
    }
}
