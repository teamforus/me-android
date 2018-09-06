package io.forus.me.android.presentation.view.screens.wallets

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRViewState

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.FragmentListener
import io.forus.me.android.presentation.internal.Injection
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 * Fragment Assign Delegates Screen.
 */
class WalletsFragment : LRFragment<WalletsModel, WalletsView, WalletsPresenter>(), WalletsView, FragmentListener {

    companion object {
        fun newIntent(): WalletsFragment {
            return WalletsFragment()
        }
    }

    private lateinit var adapter: WalletsAdapter

    override fun getTitle(): String = getString(R.string.dashboard_valuta)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)
        adapter = WalletsAdapter()
        adapter.clickListener = { item ->
            navigator.navigateToWallet(activity, item)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

    }


    override fun createPresenter() = WalletsPresenter(
            Injection.instance.walletsRepository
    )


    override fun render(vs: LRViewState<WalletsModel>) {
        super.render(vs)


        adapter.wallets = vs.model.items




    }


}

