package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityActionPaymentBinding
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.ActionsActivity


class ActionPaymentActivity : BaseActivity() {

    var info_button: View? = null
    var profile_button: io.forus.me.android.presentation.view.component.images.AutoLoadImageView? =
        null
    var toolbar_title: io.forus.me.android.presentation.view.component.text.TextView? = null

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

    private lateinit var binding: ActivityActionPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActionPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profile_button = binding.root.findViewById(R.id.profile_button)
        info_button = binding.root.findViewById(R.id.info_button)
        toolbar_title = binding.root.findViewById(R.id.toolbar_title)

        val intent = this.intent
        intent.extras.let {
            product = it?.getSerializable(ACTION_PRODUCT_EXTRA) as ProductSerializable
            voucherAddress = intent.getSerializableExtra(ActionsActivity.VOUCHER_ADDRESS_EXTRA) as String

        }

        product.let {
            replaceFragment(R.id.dashboard_content, ActionPaymentFragment.newIntent(product!!, voucherAddress!!
            ))

        }


        toolbar_title?.text = getString(R.string.payment)
        profile_button?.setImageDrawable(ContextCompat.getDrawable(this@ActionPaymentActivity, R.drawable.ic_back))
        profile_button?.setOnClickListener {
            finish()
        }


    }

    override fun replaceFragment(fragment: Fragment, sharedViews: List<View>) {
        super.replaceFragment(R.id.dashboard_top_content, fragment, sharedViews, true)
    }


    fun addPopupFragment(fragment: Fragment, title: String) {
        replaceFragment(R.id.fragmentPanelContainer, fragment)
        binding.slidingPanelTitle.text = title
        binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

}