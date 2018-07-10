package io.forus.me.android.presentation.view.screens.vouchers.item


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
class VoucherActivity : ToolbarActivity() {


    companion object {

         val ID_EXTRA = "ID_EXTRA"

        fun getCallingIntent(context: Context, id: String): Intent {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra(ID_EXTRA, id)
            return intent
        }
    }

    override val toolbarType: ToolbarType
        get() = ToolbarType.Small

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override val toolbarTitle: String
        get() = getString(R.string.voucher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = VoucherFragment.newIntent(intent.getStringExtra(ID_EXTRA))

            addFragment(R.id.fragmentContainer, fragment)
        }
    }


}
