package io.forus.me.android.presentation.view.screens.records.categories

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.list.RecordsAdapter
import kotlinx.android.synthetic.main.fragment_record_categories.*
import androidx.recyclerview.widget.DividerItemDecoration
import android.util.Log
import io.forus.me.android.presentation.view.component.dividers.FDividerItemDecoration
import io.forus.me.android.presentation.view.screens.records.categories.RecordCategoriesPresenter

import android.content.Context


class RecordCategoriesFragment : ToolbarLRFragment<RecordCategoriesModel, RecordCategoriesView, RecordCategoriesPresenter>(), RecordCategoriesView {

    companion object {
        fun newIntent(): RecordCategoriesFragment {
            return RecordCategoriesFragment()
        }
    }

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_records)


    override val allowBack: Boolean
        get() = false


    private lateinit var adapter: RecordCategoriesAdapter

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_record_categories, container, false).also {
        adapter = RecordCategoriesAdapter()
    }


    public var itemSelectListener: OnItemSelected? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        itemSelectListener = context as OnItemSelected
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.clickListener = { item ->
            adapter.selectItem(item)
            if (itemSelectListener != null) itemSelectListener!!.onItemSelected(item)

        }


        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager
        val dividerItemDecoration = FDividerItemDecoration(recycler.getContext(), R.drawable.shape_divider_item_record)

        recycler.addItemDecoration(dividerItemDecoration)

        recycler.adapter = adapter



        btn_new_record.setOnClickListener {
            this.navigator.navigateToNewRecord(activity)
        }
    }

    override fun createPresenter() = RecordCategoriesPresenter(
            Injection.instance.recordsRepository
    )

    override fun render(vs: LRViewState<RecordCategoriesModel>) {
        super.render(vs)

        adapter.items = vs.model.items
    }

    interface OnItemSelected {
        fun onItemSelected(item: io.forus.me.android.domain.models.records.RecordCategory)
    }
}

