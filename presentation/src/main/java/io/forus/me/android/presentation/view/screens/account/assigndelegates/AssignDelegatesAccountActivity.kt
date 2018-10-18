package io.forus.me.android.presentation.view.screens.account.assigndelegates


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.screens.account.assigndelegates.pin.RestoreByPinFragment
import io.forus.me.android.presentation.view.screens.account.assigndelegates.qr.RestoreByQRFragment

class AssignDelegatesAccountActivity : SlidingPanelActivity() {


    companion object {
        var assignDelegatesAccountActivity: AssignDelegatesAccountActivity? = null

        fun getInstance(): AssignDelegatesAccountActivity? {
            return assignDelegatesAccountActivity
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AssignDelegatesAccountActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val assignDelegatesAccountFragment = AssignDelegatesAccountFragment()
            addFragment(R.id.fragmentContainer, assignDelegatesAccountFragment)
        }

        assignDelegatesAccountActivity = this
    }

    fun showPopupQRFragment(){
        addPopupFragment(RestoreByQRFragment(), resources.getString(R.string.restore_title_qr))
    }

    fun showPopupPinFragment(){
        addPopupFragment(RestoreByPinFragment(), resources.getString(R.string.restore_title_pincode))
    }
}
