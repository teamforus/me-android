package io.forus.me.android.presentation.view.screens.records.types

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
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordTypesAdapter




class RecordTypesFragment : ToolbarLRFragment<RecordTypesModel, RecordTypesView, RecordTypesPresenter>(), RecordTypesView {

    companion object {
        fun newIntent(): RecordTypesFragment {
            return RecordTypesFragment()
        }
    }

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_records)


    override val allowBack: Boolean
        get() = false


    private lateinit var adapter: RecordTypesAdapter

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_record_categories, container, false).also {
        adapter = RecordTypesAdapter({
            if (itemSelectListener != null) {
                itemSelectListener!!.onItemSelected(it)
            }
        })
    }

    var showList = true

    override fun onResume() {
        super.onResume()
        Log.d("forus","onResume ")
        if(itemSelectListener != null){
            itemSelectListener!!.onRecordTypesFragmentResume()
        }
    }

    var itemSelectListener: OnItemSelected? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("forus","onAttach_context = ${context::class}")
        itemSelectListener = context as OnItemSelected
        Log.d("forus","itemSelectListener = ${itemSelectListener==null}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null) {
            showList = arguments!!.getInt("showList") != 0
        }

        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager
        val dividerItemDecoration = FDividerItemDecoration(recycler.context, R.drawable.shape_divider_item_record)

        recycler.addItemDecoration(dividerItemDecoration)

        if(showList) recycler.adapter = adapter



        btn_new_record.setOnClickListener {
            this.navigator.navigateToNewRecord(activity)
        }
    }

    override fun createPresenter() = RecordTypesPresenter(
            Injection.instance.recordsRepository
    )

    override fun render(vs: LRViewState<RecordTypesModel>) {
        super.render(vs)

        Log.d("forus","vs.model.items="+vs.model.items)
        Log.d("forus","showList"+showList)
        Log.d("forus","itemSelectListener==null=${itemSelectListener==null}")
        if(vs.model.items.isNotEmpty() && !showList && itemSelectListener != null){
            itemSelectListener!!.onRecordTypesLoaded(vs.model.items)

        }
        adapter.items = vs.model.items

    }

    interface OnItemSelected {

        fun onRecordTypesLoaded(list: List<io.forus.me.android.domain.models.records.RecordType>)

        fun onItemSelected(item: io.forus.me.android.domain.models.records.RecordType)

        fun onRecordTypesFragmentResume()
    }
}

