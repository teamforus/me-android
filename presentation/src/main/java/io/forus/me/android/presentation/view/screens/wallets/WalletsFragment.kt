package io.forus.me.android.presentation.view.screens.wallets

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRViewState

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentRecyclerBinding
import io.forus.me.android.presentation.interfaces.FragmentListener
import io.forus.me.android.presentation.internal.Injection

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

    override fun getTitle(): String = getString(R.string.dashboard_currency)

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private lateinit var binding: FragmentRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {

        binding = FragmentRecyclerBinding.inflate(inflater)

        adapter = WalletsAdapter()
        adapter.clickListener = { item ->
            navigator.navigateToWallet(activity, item)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.layoutManager =
            LinearLayoutManager(context)
        binding.recycler.adapter = adapter

    }


    override fun createPresenter() = WalletsPresenter(
            Injection.instance.walletsRepository
    )


    override fun render(vs: LRViewState<WalletsModel>) {
        super.render(vs)


        adapter.wallets = vs.model.items




    }


}

