package io.forus.me.android.presentation.view.screens.records.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import kotlinx.android.synthetic.main.fragment_records_recycler.*

/**
 * Fragment Records Delegates Screen.
 */
class RecordsFragment : ToolbarLRFragment<RecordsModel, RecordsView, RecordsPresenter>(), RecordsView{

    companion object {
        private val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
        private val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

        fun newIntent(recordCategoryId: Long, recordCategoryName: String): RecordsFragment = RecordsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(CATEGORY_ID_EXTRA, recordCategoryId)
            bundle.putString(CATEGORY_NAME_EXTRA, recordCategoryName)
            it.arguments = bundle
        }

        fun newIntent(): RecordsFragment = RecordsFragment().also {


        }
    }

    override val allowBack: Boolean
        get() = true

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_records)

    private var recordCategoryId: Long = 0
    private var recordCategoryName: String = ""

    private lateinit var adapter: RecordsAdapter

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_records_recycler, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            recordCategoryId = bundle.getLong(CATEGORY_ID_EXTRA)
            recordCategoryName = bundle.getString(CATEGORY_NAME_EXTRA)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // setToolbarTitle(recordCategoryName)

        adapter = RecordsAdapter()
        adapter.clickListener = { item ->
            navigator.navigateToRecordDetails(activity, item)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }


    override fun createPresenter() = RecordsPresenter(
            recordCategoryId,
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<RecordsModel>) {
        super.render(vs)

        adapter.records = vs.model.items
    }
}

