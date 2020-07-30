package io.forus.me.android.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.fragment_popup_qr.*


class QrFragment : BaseFragment() {

    var qrText : String = ""
        set(value) {
            if(field != value){
                field = value
                if (qrImage != null) {
                    qrImage.setQRText(value)
                }
            }
        }

    var qrHead: String = ""
    var qrSubtitle : String = ""
    var qrDescription : String = ""

    companion object {
        private val QR_TEXT = "QR_TEXT"
        private val QR_HEAD = "QR_HEAD"
        private val QR_SUBTITLE = "QR_SUBTITLE"
        private val QR_DESCRIPTION = "QR_DESCRIPTION"

        fun newIntent(qrText: String, qrHead: String?, qrSubtitle: String?, qrDescription: String?): QrFragment = QrFragment().also {
            val bundle = Bundle()
            bundle.putString(QR_TEXT, qrText)
            if(qrHead != null) bundle.putString(QR_HEAD, qrHead)
            if(qrSubtitle != null) bundle.putString(QR_SUBTITLE, qrSubtitle)
            if(qrDescription != null) bundle.putString(QR_DESCRIPTION, qrDescription)
            it.arguments = bundle
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.fragment_popup_qr
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (qrText.isNotEmpty()) {
            qrImage.setQRText(qrText)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also{
            val bundle = this.arguments
            if (bundle != null) {
                qrText = bundle.getString(QR_TEXT, "")
                qrHead = bundle.getString(QR_HEAD, "")
                qrSubtitle = bundle.getString(QR_SUBTITLE, "")
                qrDescription = bundle.getString(QR_DESCRIPTION, "")
            }
        }
    }

    override fun initUI() {
        if(qrHead.isNotBlank()) head.text = qrHead
        if(qrSubtitle.isNotBlank()) subtitle.text = qrSubtitle
        if(qrDescription.isNotBlank()) description.text = qrDescription
    }
}

