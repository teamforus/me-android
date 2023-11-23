package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentActionPaymentBinding
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ApplyActionTransactionDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.FullscreenDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ThrowableErrorDialog
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup.PriceAgreementFragment
import java.math.BigDecimal


class ActionPaymentFragment : BaseFragment() {

    companion object {

        const val ACTION_PRODUCT_EXTRA = "ACTION_PRODUCT_EXTRA"
        const val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"



        fun newIntent(product: ProductSerializable, voucherAddress: String): ActionPaymentFragment = ActionPaymentFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ACTION_PRODUCT_EXTRA, product)
            bundle.putString(VOUCHER_ADDRESS_EXTRA, voucherAddress)

            it.arguments = bundle
        }
    }


    lateinit var mainViewModel: ActionPaymentViewModel

    lateinit var binding: FragmentActionPaymentBinding

    var voucherAddress: String? = null
    var product: ProductSerializable? = null

    //var fundName: String? = null


    /* override fun getLayoutID(): Int {
         return R.layout.fragment_action_payment
     }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = product!!.photoURL
        if (url != null && url.isNotEmpty()) {
            Glide.with(context).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivActionIcon)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? 
    {
       // return super.onCreateView(inflater, container, savedInstanceState).also {
            val bundle = this.arguments
            if (bundle != null) {
                voucherAddress = bundle.getString(VOUCHER_ADDRESS_EXTRA, "")
                product = bundle.getSerializable(ACTION_PRODUCT_EXTRA) as ProductSerializable


            }

            mainViewModel = ViewModelProviders.of(this).get(ActionPaymentViewModel::class.java)

            product.let {
                mainViewModel.setProduct(it!!)
                mainViewModel.voucherAddress = voucherAddress
            }


            binding = FragmentActionPaymentBinding.inflate(inflater)
        //DataBindingUtil.inflate(
                  //  inflater, R.layout.fragment_action_payment, container, false)
            val view: View = binding.getRoot()
            binding.model = mainViewModel

            mainViewModel.confirmPayment.observe(requireActivity(), Observer {
                if (!it!!) return@Observer

                if(product!!.priceUser > BigDecimal.ZERO) {
                    showConfirmDialog()
                }else{
                    binding.btnMake.active = false
                    binding.btnMake.isEnabled = false
                    binding.progress.visibility = View.VISIBLE
                    mainViewModel.makeTransaction()
                }
            })

            mainViewModel.successPayment.observe(requireActivity(), Observer {
                if (!it!!) return@Observer
                FullscreenDialog.display(fragmentManager, getString(R.string.vouchers_apply_success), getString(R.string.vouchers_transaction_duration_of_payout), getString(R.string.me_ok)) {
                    requireActivity().finish()
                }
            })

            mainViewModel.errorPayment.observe(requireActivity(), Observer {
                binding.progress.visibility = View.GONE
                binding.btnMake.active = true
                binding.btnMake.isEnabled = true
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
                Log.d("forus", "Click price agreement")


                //(requireActivity() as ActionPaymentActivity).addPopupFragment(fragment, "")

                // activity.replaceFragment(R.id.fragmentPanelContainer, fragment)
                //showPopupQRFragment(QrCode(QrCode.Type.P2P_IDENTITY, vs.model.account.address).toJson())
                // }.show()
            })

            val fragment = PriceAgreementFragment.newIntent(product!!)

            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()


            transaction.replace(R.id.fragmentPanelContainer, fragment)
           // transaction.addToBackStack(null)
            transaction.commit()


            return view
       // }
    }




    fun showConfirmDialog() {

        var title = getString(R.string.submit_price_title)
        var toPay = Converter.convertBigDecimalToStringNL(product!!.priceUser)
        var subtitle = getString(R.string.submit_price_subtitle)



        ApplyActionTransactionDialog(requireActivity(), title, toPay, subtitle) {
            binding.btnMake.active = false
            binding.btnMake.isEnabled = false
            binding.progress.visibility = View.VISIBLE
            mainViewModel.makeTransaction()
        }.show()
    }

    override fun initUI() {
        /*if(qrHead.isNotBlank()) head.text = qrHead
        if(qrSubtitle.isNotBlank()) subtitle.text = qrSubtitle
        if(qrDescription.isNotBlank()) description.text = qrDescription*/
    }
}

