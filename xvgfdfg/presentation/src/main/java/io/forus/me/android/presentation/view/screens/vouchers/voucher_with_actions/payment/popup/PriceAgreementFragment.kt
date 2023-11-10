package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentPriceAgreementBinding
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ActionPaymentFragment
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ProductSerializable


class PriceAgreementFragment : BaseFragment() {

    lateinit var mainViewModel: PriceAgreementViewModel

    lateinit var binding: FragmentPriceAgreementBinding


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also{
            val bundle = this.arguments
            if (bundle != null) {
                product = bundle.getSerializable(ActionPaymentFragment.ACTION_PRODUCT_EXTRA)  as ProductSerializable
            }
            binding = FragmentPriceAgreementBinding.inflate(inflater)

            return binding.root

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


    override fun initUI() {
    }
}

