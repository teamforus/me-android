package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentReservationVouchersRecyclerBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersAdapter
import io.forus.me.android.presentation.view.screens.vouchers.provider.ProviderActivity


class ProductReservationFragment : ToolbarLRFragment<ProductReservationModel, ProductReservationView, ProductReservationPresenter>(), ProductReservationView,
    MViewModelProvider<VoucherViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[VoucherViewModel::class.java].apply { }
    }

    private lateinit var address: String
    private var showParentVoucher: Boolean = false



    companion object {
        private val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        private val SHOW_PARENT_VOUCHER = "SHOW_PARENT_VOUCHER"

        fun newIntent(id: String, showParentVoucher: Boolean): ProductReservationFragment = ProductReservationFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(VOUCHER_ADDRESS_EXTRA, id)
            bundle.putSerializable(SHOW_PARENT_VOUCHER, showParentVoucher)
            it.arguments = bundle
        }
    }

    private lateinit var adapter: VouchersAdapter

    override val toolbarTitle: String
        get() = getString(R.string.qr_choose_reservation)


    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private lateinit var binding: FragmentReservationVouchersRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentReservationVouchersRecyclerBinding.inflate(inflater)

        address = if (arguments == null) "" else requireArguments().getString(VOUCHER_ADDRESS_EXTRA, "")
        showParentVoucher = if (arguments == null) false else requireArguments().getBoolean(SHOW_PARENT_VOUCHER, false)

        adapter = VouchersAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.clickListener = { voucher: Voucher, sharedViews: List<View>, position: Int ->
            if(context != null ) {
                val intentToLaunch = ProviderActivity.getCallingIntent(requireContext(), voucher.address!!)
                requireContext().startActivity(intentToLaunch)
            }
        }
        binding.recycler.layoutManager =
            LinearLayoutManager(context)
        binding.recycler.adapter = adapter

        if (showParentVoucher) {
            binding.parentVoucherBt.visibility = View.VISIBLE
            binding.parentVoucherBt.setOnClickListener {
                if (context != null) {
                    navigator.navigateToVoucherProvider(context, address)
                }
            }
        }else{
            binding.parentVoucherBt.visibility = View.GONE
        }

    }


    override fun createPresenter() = ProductReservationPresenter(
            Injection.instance.vouchersRepository , viewModel.address.value?:"",
        Injection.instance.firestoreTokenManager
    )


    override fun onResume() {
        super.onResume()
        super.updateModel()

    }

    override fun render(vs: LRViewState<ProductReservationModel>) {
        super.render(vs)

        binding.tvNoVouchers.visibility = if (!vs.loading && vs.loadingError == null && vs.model.items.isEmpty()) View.VISIBLE else View.INVISIBLE

        adapter.vouchers = vs.model.items

        if(vs.closeScreen) closeScreen()
    }

    private fun closeScreen() {
        showToastMessage(resources.getString(R.string.vouchers_apply_success))
        activity?.finish()
    }
}

