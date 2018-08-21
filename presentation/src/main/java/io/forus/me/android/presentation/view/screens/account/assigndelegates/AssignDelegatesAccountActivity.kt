package io.forus.me.android.presentation.view.screens.account.assigndelegates


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentActionListener

import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.forus.me.android.presentation.view.screens.account.pin.RestoreByPinFragment
import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*

/**
 * Main application screen. This is the app entry point.
 */
class AssignDelegatesAccountActivity : CommonActivity() {


    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AssignDelegatesAccountActivity::class.java)
        }
    }

    private lateinit var assignDelegatesAccountFragment: AssignDelegatesAccountFragment

    override val viewID: Int
        get() = R.layout.activity_toolbar_sliding_panel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            assignDelegatesAccountFragment = AssignDelegatesAccountFragment()
            addFragment(R.id.fragmentContainer, assignDelegatesAccountFragment)
        }
    }

    fun showPopupQRFragment(code: String){
        replaceFragment(R.id.fragmentPanelContainer, QrFragment.newIntent(code))

        sliding_panel_title.text = "QR code"
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    fun showPopupPinFragment(){
        replaceFragment(R.id.fragmentPanelContainer, RestoreByPinFragment())

        sliding_panel_title.text = "PIN code"
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }
}
