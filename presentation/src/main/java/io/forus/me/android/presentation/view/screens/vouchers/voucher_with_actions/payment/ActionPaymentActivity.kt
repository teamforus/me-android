package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityActionPaymentBinding
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.ActionsActivity
import kotlinx.android.synthetic.main.toolbar_view.*


class ActionPaymentActivity : AppCompatActivity() {




    companion object {

        val ACTION_PRODUCT_EXTRA = "ACTION_PRODUCT_EXTRA"
        val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"

        fun getCallingIntent(context: Context, product: ProductSerializable, voucherAddress: String): Intent {
            val intent = Intent(context, ActionPaymentActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(ACTION_PRODUCT_EXTRA, product)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, voucherAddress)
            intent.putExtras(bundle)

            return intent
        }
    }


    lateinit var mainViewModel: ActionPaymentViewModel

    lateinit var binding: ActivityActionPaymentBinding

    var voucherAddress: String? = null
    var product: ProductSerializable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_payment)

        val intent = this.intent
        intent.extras.let {
            product = it.getSerializable(ACTION_PRODUCT_EXTRA) as ProductSerializable
            voucherAddress = intent.getSerializableExtra(ActionsActivity.VOUCHER_ADDRESS_EXTRA) as String
        }

        mainViewModel = ViewModelProviders.of(this).get(ActionPaymentViewModel::class.java)

        product.let {
            mainViewModel.setProduct(it!!)
            mainViewModel.voucherAddress = voucherAddress
        }

        binding = DataBindingUtil.setContentView<ActivityActionPaymentBinding>(this@ActionPaymentActivity, R.layout.activity_action_payment)
        binding.lifecycleOwner = this
        binding.model = mainViewModel



        toolbar_title.text = getString(R.string.payment)
        profile_button.setImageDrawable(ContextCompat.getDrawable(this@ActionPaymentActivity, R.drawable.ic_back))
        profile_button.setOnClickListener {
            finish()
        }

        mainViewModel.success.observe(this@ActionPaymentActivity, Observer {
            Log.d("forus","NOTE="+it)
        })

        mainViewModel.error.observe(this@ActionPaymentActivity, Observer {
            Log.d("forus","NOTE="+it)
        })

        mainViewModel.note.observe(this@ActionPaymentActivity, Observer {
            Log.d("forus","NOTE="+it)
        })

    }
}