package io.forus.me.android.presentation.view.screens.records.categories

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
import io.forus.me.android.presentation.interfaces.ToolbarListener
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 * Fragment Records Delegates Screen.
 */
class RecordCategoriesFragment : LRFragment<RecordCategoriesModel, RecordCategoriesView, RecordCategoriesPresenter>(), RecordCategoriesView, FragmentListener, ToolbarListener {

    companion object {
        fun newIntent(): RecordCategoriesFragment {
            return RecordCategoriesFragment()
        }
    }

    override val subviewFragment: BaseFragment?
        get() = null
    override val pageTitle: String
        get() = getString(R.string.records)

    private lateinit var adapter: RecordCategoriesAdapter

    override fun getTitle(): String = getString(R.string.valuta)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_recycler, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecordCategoriesAdapter()
        adapter.clickListener = { item ->
            //navigator.navigateToRecord(activity, item.id)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

    }


    override fun createPresenter() = RecordCategoriesPresenter(
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<RecordCategoriesModel>) {
        super.render(vs)


        adapter.items = vs.model.items




    }


}

