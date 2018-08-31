package io.forus.me.android.presentation.view.screens.vouchers.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import kotlinx.android.synthetic.main.vouchers_fragment_recycler.*

/**
 * Fragment Vouchers Delegates Screen.
 */
class VouchersFragment : ToolbarLRFragment<VouchersModel, VouchersView, VouchersPresenter>(), VouchersView{

    companion object {
        fun newIntent(): VouchersFragment {
            return VouchersFragment()
        }
    }

    private lateinit var adapter: VouchersAdapter

    override val toolbarTitle: String
        get() = getString(R.string.vouchers)


    override val allowBack: Boolean
        get() = false

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.vouchers_fragment_recycler, container, false).also {
        adapter = VouchersAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.clickListener = { item ->
            navigator.navigateToVoucher(activity, item.address)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

    }


    override fun createPresenter() = VouchersPresenter(
            Injection.instance.vouchersRepository
    )


    override fun render(vs: LRViewState<VouchersModel>) {
        super.render(vs)

        tv_no_vouchers.visibility = if(vs.model.items.isEmpty()) View.VISIBLE else View.INVISIBLE

        adapter.vouchers = vs.model.items
    }
}

