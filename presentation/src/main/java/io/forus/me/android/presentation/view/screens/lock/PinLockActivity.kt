package io.forus.me.android.presentation.view.screens.lock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.screens.lock.fingerprint.FingerprintFragment
import io.forus.me.android.presentation.view.screens.lock.pin.PinLockFragment

class PinLockActivity : CommonActivity() {


    companion object {
        private val LOCK_INTENT_EXTRA = "LOCK_INTENT_EXTRA"
        private val USE_FINGERPRINT = "USE_FINGERPRINT"

        fun getCallingIntent(context: Context, navigationIntent: Intent, useFingerprint: Boolean): Intent {
            val intent = Intent(context, PinLockActivity::class.java)
            intent.putExtra(LOCK_INTENT_EXTRA, navigationIntent)
            intent.putExtra(USE_FINGERPRINT, useFingerprint)
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
            val useFingerprint = intent.getBooleanExtra(USE_FINGERPRINT, false)
            if(useFingerprint){
                useFingerprint()
            }
            else{
                usePinlock()
            }
        }
    }

    fun useFingerprint(){
        val fragment = FingerprintFragment.newIntent()
        replaceFragment(R.id.fragmentContainer, fragment)
    }

    fun usePinlock(){
        val fragment = PinLockFragment.newIntent()
        replaceFragment(R.id.fragmentContainer, fragment)
    }

    fun unlockSuccess(){
        this.startActivity(navigationIntent)
        finish()
    }
}