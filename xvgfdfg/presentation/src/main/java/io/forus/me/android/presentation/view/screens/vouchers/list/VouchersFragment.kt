package io.forus.me.android.presentation.view.screens.vouchers.list

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentVouchersRecyclerBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.component.images.AutoLoadImageView
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.TransactionsActivity


/**
 * Fragment Vouchers Delegates Screen.
 */
class VouchersFragment : ToolbarLRFragment<VouchersModel, VouchersView, VouchersPresenter>(), VouchersView,
    MViewModelProvider<VoucherViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[VoucherViewModel::class.java].apply { }
    }

    companion object {
        fun newIntent(): VouchersFragment {
            return VouchersFragment()
        }
    }

    private lateinit var adapter: VouchersAdapter

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_vouchers)


    override val allowBack: Boolean
        get() = false

    override val showAccount: Boolean
        get() = false

    override val showInfo: Boolean
        get() = true

     override var info_button: AutoLoadImageView? = null

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private lateinit var binding: FragmentVouchersRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentVouchersRecyclerBinding.inflate(inflater)

        adapter = VouchersAdapter()

        info_button = binding.root.findViewById(R.id.info_button)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.clickListener = { voucher: Voucher, sharedViews: List<View>, position: Int ->

            viewModel.setVoucher(voucher)
            viewModel.setAddress(voucher.address?:"")

            (activity as? BaseActivity)?.replaceFragment(VoucherFragment.newInstance(voucher, position), sharedViews)
        }
        binding.recycler.layoutManager =
            LinearLayoutManager(context)
        binding.recycler.adapter = adapter

        info_button?.setImageResource(R.drawable.ic_transactions)
        info_button?.setOnClickListener {
            startActivity(TransactionsActivity.getCallingIntent(requireContext()))
        }

    }


    override fun createPresenter() = VouchersPresenter(
            Injection.instance.vouchersRepository
    )


    override fun render(vs: LRViewState<VouchersModel>) {
        super.render(vs)

        binding.tvNoVouchers.visibility = if (!vs.loading && vs.loadingError == null && vs.model.items.isEmpty()) View.VISIBLE else View.INVISIBLE

        adapter.vouchers = vs.model.items
    }
}

