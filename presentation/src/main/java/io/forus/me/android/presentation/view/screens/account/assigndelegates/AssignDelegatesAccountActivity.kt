package io.forus.me.android.presentation.view.screens.account.assigndelegates


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentActionListener

import io.forus.me.android.presentation.view.activity.ToolbarActivity
import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*

/**
 * Main application screen. This is the app entry point.
 */
class AssignDelegatesAccountActivity : ToolbarActivity() {


    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AssignDelegatesAccountActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar_sliding_panel

    override val toolbarTitle: String
        get() = getString(R.string.login)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = AssignDelegatesAccountFragment()
            fragment.slidingToolbarFragmentActionListener = object : SlidingToolbarFragmentActionListener {
                override fun openPanel() {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
            }
            addFragment(R.id.fragmentContainer, fragment)

            addFragment(R.id.fragmentPanelContainer, fragment.slidingFragment)
            sliding_panel_title.text = fragment.slidingPanelTitle
        }
    }


}
