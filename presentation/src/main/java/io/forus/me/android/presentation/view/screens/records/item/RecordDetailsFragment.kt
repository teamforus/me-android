package io.forus.me.android.presentation.view.screens.records.item

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentActionListener
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentListener
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_record_detail.*

class RecordDetailsFragment : ToolbarLRFragment<RecordDetailsModel, RecordDetailsView, RecordDetailsPresenter>(), RecordDetailsView, SlidingToolbarFragmentListener {

    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA";

        fun newIntent(recordId: Long): RecordDetailsFragment = RecordDetailsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(RECORD_ID_EXTRA, recordId)
            it.arguments = bundle
        }
    }

    override val allowBack: Boolean
        get() = true

    override val toolbarTitle: String
        get() = getString(R.string.title_record_detail)

    private var recordId: Long = 0

    private lateinit var adapter: ValidatorsAdapter

    private  var qrFragment: QrFragment =  QrFragment.newIntent()
    var slidingToolbarFragmentActionListener : SlidingToolbarFragmentActionListener? = null

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_record_detail, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            recordId = bundle.getLong(RECORD_ID_EXTRA)
        }

        adapter = ValidatorsAdapter()
        adapter.clickListener = { item ->

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_show_qr.setOnClickListener {
            slidingToolbarFragmentActionListener?.openPanel()
        }

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    override fun createPresenter() = RecordDetailsPresenter(
            recordId,
            Injection.instance.recordsRepository,
            Injection.instance.validationRepository
    )

    override fun render(vs: LRViewState<RecordDetailsModel>) {
        super.render(vs)

        progressBar.visibility = if (vs.loading || vs.model.creatingQrCode) View.VISIBLE else View.INVISIBLE

        val record = vs.model.item
        type.text = record?.recordType?.name
        value.text = record?.value
        adapter.validators = vs.model.validators

        if (vs.model.uuid != null && vs.model.uuid.isNotEmpty())
            qrFragment.qrText = vs.model.uuid

        if(vs.model.qrCode != null) qr_code.setImageBitmap(vs.model.qrCode)
    }

    override fun getSlidingFragment(): BaseFragment {
        return  qrFragment
    }

    override fun getSlidingPanelTitle(): String {
        return "QR code"
    }
}