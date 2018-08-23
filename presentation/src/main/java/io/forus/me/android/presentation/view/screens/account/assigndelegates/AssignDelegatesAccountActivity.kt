package io.forus.me.android.presentation.view.screens.account.assigndelegates


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.screens.account.assigndelegates.pin.RestoreByPinFragment
import io.forus.me.android.presentation.view.screens.account.assigndelegates.qr.RestoreByQRFragment
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

        fragmentPanelContainer.minimumHeight = Converter.convertDpToPixel(500f, applicationContext)
        sliding_layout.addPanelSlideListener(object: SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    removePopupFragment()
                }
            }

        })
    }

    fun showPopupQRFragment(){
        replaceFragment(R.id.fragmentPanelContainer, RestoreByQRFragment())
        expandPanel("QR-Code")
    }

    fun showPopupPinFragment(){
        replaceFragment(R.id.fragmentPanelContainer, RestoreByPinFragment())
        expandPanel("Pincode")
    }

    private fun expandPanel(title: String){
        sliding_panel_title.text = title
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    private fun removePopupFragment(){
        removeFragment(R.id.fragmentPanelContainer)
    }
}
