package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentActionPaymentBinding
import io.forus.me.android.presentation.databinding.FragmentPriceAgreementBinding
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ActionPaymentFragment
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ActionPaymentViewModel
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ProductSerializable
import kotlinx.android.synthetic.main.fragment_popup_qr.*


class PriceAgreementFragment : BaseFragment() {

    lateinit var mainViewModel: PriceAgreementViewModel

    lateinit var binding: FragmentPriceAgreementBinding//ActivityActionPaymentBinding


    companion object {

        const val ACTION_PRODUCT_EXTRA = "ACTION_PRODUCT_EXTRA"

        fun newIntent(product: ProductSerializable): PriceAgreementFragment = PriceAgreementFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ACTION_PRODUCT_EXTRA, product)
            it.arguments = bundle
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.fragment_price_agreement
    }


    var product: ProductSerializable? = null
   // var fundName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also{
            val bundle = this.arguments
            if (bundle != null) {
                product = bundle.getSerializable(ActionPaymentFragment.ACTION_PRODUCT_EXTRA)  as ProductSerializable
               // fundName = bundle.getString(ActionPaymentFragment.VOUCHER_FUND_NAME_EXTRA,"")
            }
            //binding = DataBindingUtil.inflate(
                 //   inflater, R.layout.fragment_price_agreement, container, false)

            val viewRoot = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_price_agreement, null)
            binding = FragmentPriceAgreementBinding.bind(viewRoot)//inflate()//(inflater, viewRoot!!,  false)
            val view = binding.root

            return view

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(PriceAgreementViewModel::class.java)

        product.let {
            mainViewModel.setProduct(it!!)
        }



        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.model = mainViewModel
    }



    /* override fun onCreateView(
             inflater: LayoutInflater,
             container: ViewGroup?,
             savedInstanceState: Bundle?
     ): View? {
         Log.d("my", "onCreateView_____")
         mainViewModel =
                 ViewModelProviders.of(this).get(PriceAgreementViewModel::class.java)
         val root = inflater.inflate(R.layout.fragment_price_agreement, container, false)
         //val textView: TextView = root.findViewById(R.id.text_dashboard)

        /* mainViewModel.text.observe(viewLifecycleOwner, Observer {
             //  textView.text = it
         })*/


         return root
     }*/

    override fun initUI() {
        //if(qrHead.isNotBlank()) head.text = qrHead
       // if(qrSubtitle.isNotBlank()) subtitle.text = qrSubtitle
       // if(qrDescription.isNotBlank()) description.text = qrDescription
    }
}

