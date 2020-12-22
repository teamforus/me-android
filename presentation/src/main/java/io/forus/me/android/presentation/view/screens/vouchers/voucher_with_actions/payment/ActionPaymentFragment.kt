package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentActionPaymentBinding
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ApplyActionTransactionDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.FullscreenDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ThrowableErrorDialog
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup.PriceAgreementFragment
import java.text.NumberFormat
import java.util.*


class ActionPaymentFragment : BaseFragment() {

    companion object {

        const val ACTION_PRODUCT_EXTRA = "ACTION_PRODUCT_EXTRA"
        const val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        const val VOUCHER_FUND_NAME_EXTRA = "VOUCHER_ADDRESS_EXTRA"

        fun getCallingIntent(context: Context, product: ProductSerializable, voucherAddress: String, fundName: String): Intent {
            val intent = Intent(context, ActionPaymentActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(ACTION_PRODUCT_EXTRA, product)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, voucherAddress)
            intent.putExtras(bundle)
            intent.putExtra(VOUCHER_FUND_NAME_EXTRA, fundName)

            return intent
        }

        fun newIntent(product: ProductSerializable, voucherAddress: String): ActionPaymentFragment = ActionPaymentFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ACTION_PRODUCT_EXTRA, product)
            bundle.putString(VOUCHER_ADDRESS_EXTRA, voucherAddress)
            it.arguments = bundle
        }
    }


    lateinit var mainViewModel: ActionPaymentViewModel

    lateinit var binding: FragmentActionPaymentBinding//ActivityActionPaymentBinding

    var voucherAddress: String? = null
    var product: ProductSerializable? = null

    var fundName: String? = null


   /* override fun getLayoutID(): Int {
        return R.layout.fragment_action_payment
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also{
            val bundle = this.arguments
            if (bundle != null) {
                voucherAddress = bundle.getString(VOUCHER_ADDRESS_EXTRA, "")
                product = bundle.getSerializable(ACTION_PRODUCT_EXTRA)  as ProductSerializable

                fundName = bundle.getString(VOUCHER_FUND_NAME_EXTRA,"")

            }

            mainViewModel = ViewModelProviders.of(this).get(ActionPaymentViewModel::class.java)

            product.let {
                mainViewModel.setProduct(it!!)
                mainViewModel.voucherAddress = voucherAddress
            }


            binding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_action_payment, container, false)
            val view: View = binding.getRoot()
            binding.model = mainViewModel

            mainViewModel.confirmPayment.observe(requireActivity(), Observer {
                if (!it!!) return@Observer
                ApplyActionTransactionDialog(requireActivity(), NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                        .format(product!!.price)+"?", (product!!.price.toDouble() == 0.0)) {
                    mainViewModel.makeTransaction()
                }.show()
            })

            mainViewModel.successPayment.observe(requireActivity(), Observer {
                if (!it!!) return@Observer
                FullscreenDialog.display(fragmentManager, getString(R.string.success), "", getString(R.string.me_ok)) {
                    requireActivity().finish()
                }
            })

            mainViewModel.errorPayment.observe(requireActivity(), Observer {
                if (it != null) {
                    ThrowableErrorDialog(it, requireActivity(), object : DialogInterface.OnDismissListener, () -> Unit {
                        override fun invoke() {
                        }

                        override fun onDismiss(p0: DialogInterface?) {
                        }
                    }).show()
                }
            })

            mainViewModel.showPriceAgreement.observe(requireActivity(), Observer {
                if (!it!!) return@Observer
                Log.d("forus","Click price agreement")
                (requireActivity() as ActionPaymentActivity).addPopupFragment(PriceAgreementFragment.newIntent(product!!, fundName!!), "")
                //showPopupQRFragment(QrCode(QrCode.Type.P2P_IDENTITY, vs.model.account.address).toJson())
                // }.show()
            })

            return view
        }
    }

    override fun initUI() {
        /*if(qrHead.isNotBlank()) head.text = qrHead
        if(qrSubtitle.isNotBlank()) subtitle.text = qrSubtitle
        if(qrDescription.isNotBlank()) description.text = qrDescription*/
    }
}

