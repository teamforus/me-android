package io.forus.me.android.presentation.view.screens.assets

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
class AssetsFragment : LRFragment<AssetsModel, AssetsView, AssetsPresenter>(), AssetsView, FragmentListener {

    companion object {
        fun newIntent(): AssetsFragment {
            return AssetsFragment()
        }
    }

    private lateinit var adapter: AssetsAdapter

    override fun getTitle(): String = getString(R.string.dashboard_currency)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_recycler, container, false).also {
        adapter = AssetsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager =
            LinearLayoutManager(context)
        recycler.adapter = adapter
    }


    override fun createPresenter() = AssetsPresenter(
            Injection.instance.assetsRepository
    )


    override fun render(vs: LRViewState<AssetsModel>) {
        super.render(vs)


        adapter.assets = vs.model.items




    }


}

