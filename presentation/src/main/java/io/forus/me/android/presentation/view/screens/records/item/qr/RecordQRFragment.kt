package io.forus.me.android.presentation.view.screens.records.item.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_popup_qr.*

class RecordQRFragment : LRFragment<RecordQRModel, RecordQRView, RecordQRPresenter>(), RecordQRView {

    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA"

        fun newIntent(recordId: Long): RecordQRFragment = RecordQRFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(RECORD_ID_EXTRA, recordId)
            it.arguments = bundle
        }
    }

    private var recordId: Long = 0

    var qrText : String = ""
        set(value) {
            if(field != value){
                field = value
                if (qrImage != null) {
                    qrImage.setQRText(QrCode(QrCode.Type.P2P_RECORD, value).toJson())
                }
            }
        }

    val disposableHolder = DisposableHolder()


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_popup_qr, container, false).also {
        val bundle = this.arguments
        if (bundle != null) {
            recordId = bundle.getLong(RECORD_ID_EXTRA)
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposableHolder.disposeAll()
    }

    override fun createPresenter() = RecordQRPresenter(
            recordId,
            disposableHolder,
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<RecordQRModel>) {
        super.render(vs)

        if (vs.model.uuid != null && vs.model.uuid.isNotBlank()) {
            qrText = vs.model.uuid
        }

        if(vs.closeScreen && vs.model.recordValidatedState != null){
            closeScreen(vs.model.recordValidatedState)
        }
    }

    fun closeScreen(state: Validation.State) {
        showToastMessage(resources.getString(if(state == Validation.State.approved) R.string.record_details_validation_approved else R.string.record_details_validation_declined))
        (activity as? RecordDetailsActivity)?.closeQRFragment()
    }
}