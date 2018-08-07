package io.forus.me.android.presentation.view.screens.records.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 * Fragment Records Delegates Screen.
 */
class RecordsFragment : LRFragment<RecordsModel, RecordsView, RecordsPresenter>(), RecordsView{

    companion object {
        private val CATEGORY_EXTRA = "CATEGORY_EXTRA";

        fun newIntent(recordCategory: RecordCategory): RecordsFragment = RecordsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(CATEGORY_EXTRA, recordCategory)
            it.arguments = bundle
        }
    }

    private lateinit var recordCategory: RecordCategory

    private lateinit var adapter: RecordsAdapter

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_records_recycler, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            recordCategory = bundle.getSerializable(CATEGORY_EXTRA) as RecordCategory
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecordsAdapter()
        adapter.clickListener = { item ->
            showToastMessage(item.id.toString())
            //navigator.navigateToRecord(activity, item.id)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }


    override fun createPresenter() = RecordsPresenter(
            recordCategory,
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<RecordsModel>) {
        super.render(vs)


        adapter.records = vs.model.items




    }


}

