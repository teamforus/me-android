package io.forus.me.android.presentation.view.screens.vouchers.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.FragmentListener
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 * Fragment Vouchers Delegates Screen.
 */
class VouchersFragment : LRFragment<VouchersModel, VouchersView, VouchersPresenter>(), VouchersView, FragmentListener {

    companion object {
        fun newIntent(): VouchersFragment {
            return VouchersFragment()
        }
    }

    private lateinit var adapter: VouchersAdapter

    override fun getTitle(): String = getString(R.string.valuta)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_recycler, container, false).also {
        adapter = VouchersAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.clickListener = { item ->
            navigator.navigateToVoucher(activity, item.id)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

    }


    override fun createPresenter() = VouchersPresenter(
            Injection.instance.vouchersRepository
    )


    override fun render(vs: LRViewState<VouchersModel>) {
        super.render(vs)


        adapter.vouchers = vs.model.items




    }


}

