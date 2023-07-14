package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.ActionsActivity
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup.PriceAgreementFragment
import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*
import kotlinx.android.synthetic.main.toolbar_view.*


class ActionPaymentActivity : BaseActivity() {


    companion object {

        const val ACTION_PRODUCT_EXTRA = "ACTION_PRODUCT_EXTRA"
        const val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"


        fun getCallingIntent(context: Context, product: ProductSerializable, voucherAddress: String): Intent {
            val intent = Intent(context, ActionPaymentActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(ACTION_PRODUCT_EXTRA, product)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, voucherAddress)


            intent.putExtras(bundle)

            return intent
        }
    }


    var voucherAddress: String? = null
    var product: ProductSerializable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_payment)

        val intent = this.intent
        intent.extras.let {
            product = it?.getSerializable(ACTION_PRODUCT_EXTRA) as ProductSerializable
            voucherAddress = intent.getSerializableExtra(ActionsActivity.VOUCHER_ADDRESS_EXTRA) as String

        }

        product.let {
            replaceFragment(R.id.dashboard_content, ActionPaymentFragment.newIntent(product!!, voucherAddress!!
            ))

        }


        toolbar_title.text = getString(R.string.payment)
        profile_button.setImageDrawable(ContextCompat.getDrawable(this@ActionPaymentActivity, R.drawable.ic_back))
        profile_button.setOnClickListener {
            finish()
        }


    }

    override fun replaceFragment(fragment: Fragment, sharedViews: List<View>) {
        super.replaceFragment(R.id.dashboard_top_content, fragment, sharedViews, true)
    }


    fun addPopupFragment(fragment: Fragment, title: String) {
        replaceFragment(R.id.fragmentPanelContainer, fragment)
        sliding_panel_title.text = title
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    /*fun showPriceAgreementragment(address: String,qrHead: String? = null, qrSubtitle: String? = null, qrDescription: String? = null) {
        addPopupFragment(PriceAgreementFragment.newIntent(address, qrHead, qrSubtitle, qrDescription), "QR code")
    }*/

/*fun showPriceAgreementFragment(address: String,qrHead: String? = null, qrSubtitle: String? = null, qrDescription: String? = null) {
    addPopupFragment(QrFragment.newIntent(address, qrHead, qrSubtitle, qrDescription), "QR code")
}

override fun replaceFragment(fragment: Fragment, sharedViews: List<View>) {
    super.replaceFragment(R.id.dashboard_top_content, fragment, sharedViews, true)
}*/


    /* protected fun replaceFragment(containerViewId: Int, fragment: Fragment) {
         supportFragmentManager
                 .beginTransaction()
                 .replace(containerViewId, fragment)
                 .commit()
     }*/

}