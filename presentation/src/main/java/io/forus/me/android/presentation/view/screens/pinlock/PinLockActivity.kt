package io.forus.me.android.presentation.view.screens.pinlock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

class PinLockActivity : CommonActivity() {


    companion object {
        private val LOCK_INTENT_EXTRA = "LOCK_INTENT_EXTRA"

        fun getCallingIntent(context: Context, navigationIntent: Intent): Intent {
            val intent = Intent(context, PinLockActivity::class.java)
            intent.putExtra(LOCK_INTENT_EXTRA, navigationIntent)
            return intent
        }
    }

    private lateinit var navigationIntent: Intent

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigationIntent = intent.getParcelableExtra(LOCK_INTENT_EXTRA)
            val fragment = PinLockFragment.newIntent()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    fun unlockSuccess(){
        this.startActivity(navigationIntent)
        finish()
    }
}