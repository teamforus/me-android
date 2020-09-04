package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityActionPaymentBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherBaseErrorDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ApplyActionTransactionDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.FullscreenDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ThrowableErrorDialog
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.ActionsActivity
import kotlinx.android.synthetic.main.toolbar_view.*
import java.text.NumberFormat
import java.util.*


class ActionPaymentActivity : AppCompatActivity() {


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

        binding = DataBindingUtil.setContentView(this@ActionPaymentActivity, R.layout.activity_action_payment)
        binding.lifecycleOwner = this
        binding.model = mainViewModel



        toolbar_title.text = getString(R.string.payment)
        profile_button.setImageDrawable(ContextCompat.getDrawable(this@ActionPaymentActivity, R.drawable.ic_back))
        profile_button.setOnClickListener {
            finish()
        }

        mainViewModel.confirmPayment.observe(this@ActionPaymentActivity, Observer {
            if (!it!!) return@Observer
            ApplyActionTransactionDialog(this@ActionPaymentActivity, NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                    .format(product!!.price)+"?") {
                mainViewModel.makeTransaction()
            }.show()
        })

        mainViewModel.successPayment.observe(this@ActionPaymentActivity, Observer {
            if (!it!!) return@Observer
            FullscreenDialog.display(supportFragmentManager, getString(R.string.success), "", getString(R.string.me_ok)) {
                finish()
            }
        })

        mainViewModel.errorPayment.observe(this@ActionPaymentActivity, Observer {
            if (it != null) {
                ThrowableErrorDialog(it, this@ActionPaymentActivity, object : DialogInterface.OnDismissListener, () -> Unit {
                    override fun invoke() {
                    }

                    override fun onDismiss(p0: DialogInterface?) {
                    }
                }).show()
            }
        })


    }
}