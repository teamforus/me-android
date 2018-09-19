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

    companion object {
        private val QR_TEXT = "QR_TEXT"

        fun newIntent(): QrFragment {
            return QrFragment()
        }

        fun newIntent(qrText: String): QrFragment = QrFragment().also {
            val bundle = Bundle()
            bundle.putString(QR_TEXT, qrText)
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
                qrText = bundle.getString(QR_TEXT)
            }
        }
    }

    override fun initUI() {

    }
}

