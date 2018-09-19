package io.forus.me.android.presentation.view.screens.records.categories

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.list.RecordsAdapter
import kotlinx.android.synthetic.main.fragment_record_categories.*


class RecordCategoriesFragment : ToolbarLRFragment<RecordCategoriesModel, RecordCategoriesView, RecordCategoriesPresenter>(), RecordCategoriesView{

    companion object {
        fun newIntent(): RecordCategoriesFragment {
            return RecordCategoriesFragment()
        }
    }

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_records)


    override val allowBack: Boolean
        get() = false


    //private lateinit var adapter: RecordCategoriesAdapter
    private lateinit var adapter: RecordsAdapter

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_record_categories, container, false).also {
        //adapter = RecordCategoriesAdapter()
        adapter = RecordsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter.clickListener = { item ->
//            navigator.navigateToRecordsList(activity, item)
//        }
//        recycler.layoutManager = LinearLayoutManager(context)
//        recycler.adapter = adapter

        adapter.clickListener = { item ->
            navigator.navigateToRecordDetails(activity, item)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        btn_new_record.setOnClickListener{
            this.navigator.navigateToNewRecord(activity)
        }
    }

    override fun createPresenter() = RecordCategoriesPresenter(
            Injection.instance.recordsRepository
    )

    override fun render(vs: LRViewState<RecordCategoriesModel>) {
        super.render(vs)

        //adapter.items = vs.model.items
        adapter.records = vs.model.items
    }
}

