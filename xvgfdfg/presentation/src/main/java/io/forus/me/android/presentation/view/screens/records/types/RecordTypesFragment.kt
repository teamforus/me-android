package io.forus.me.android.presentation.view.screens.records.types

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentRecordCategoriesBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.component.dividers.FDividerItemDecoration
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordTypesAdapter


class RecordTypesFragment :
    ToolbarLRFragment<RecordTypesModel, RecordTypesView, RecordTypesPresenter>(), RecordTypesView {

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

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private lateinit var binding: FragmentRecordCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordCategoriesBinding.inflate(layoutInflater)

        adapter = RecordTypesAdapter {
            if (itemSelectListener != null) {
                itemSelectListener!!.onItemSelected(it)
            }
        }

        return binding.root
    }

    var showList = true

    override fun onResume() {
        super.onResume()
        if (itemSelectListener != null) {
            itemSelectListener!!.onRecordTypesFragmentResume()
        }
    }

    var itemSelectListener: OnItemSelected? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        itemSelectListener = context as OnItemSelected
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            showList = arguments!!.getInt("showList") != 0
        }

        val layoutManager =
            LinearLayoutManager(context)
        binding.recycler.layoutManager = layoutManager
        val dividerItemDecoration =
            FDividerItemDecoration(binding.recycler.context, R.drawable.shape_divider_item_record)

        binding.recycler.addItemDecoration(dividerItemDecoration)

        if (showList) binding.recycler.adapter = adapter




        binding.btnNewRecord.setOnClickListener {
            this.navigator.navigateToNewRecord(activity)
        }
    }

    override fun createPresenter() = RecordTypesPresenter(
        Injection.instance.recordsRepository
    )

    override fun render(vs: LRViewState<RecordTypesModel>) {
        super.render(vs)

        if (vs.model.items.isNotEmpty() && !showList && itemSelectListener != null) {
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

