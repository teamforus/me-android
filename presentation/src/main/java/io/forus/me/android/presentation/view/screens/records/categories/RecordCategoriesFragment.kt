package io.forus.me.android.presentation.view.screens.records.categories

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

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private lateinit var binding: FragmentRecordCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentRecordCategoriesBinding.inflate(layoutInflater)

        adapter = RecordCategoriesAdapter()

        return binding.root
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


        val layoutManager =
            LinearLayoutManager(context)
        binding.recycler.layoutManager = layoutManager
        val dividerItemDecoration = FDividerItemDecoration(binding.recycler.getContext(), R.drawable.shape_divider_item_record)

        binding.recycler.addItemDecoration(dividerItemDecoration)

        binding.recycler.adapter = adapter



        binding.btnNewRecord.setOnClickListener {
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

