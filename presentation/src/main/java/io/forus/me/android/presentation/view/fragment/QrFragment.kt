package io.forus.me.android.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentPopupQrBinding



class QrFragment : BaseFragment() {


    var qrText: String = ""
        set(value) {
            if (field != value) {
                field = value
                if (binding.qrImage != null) {
                    binding.qrImage?.setQRText(value)
                }
            }
        }

    var qrHead: String = ""
    var qrSubtitle: String = ""
    var qrDescription: String = ""

    companion object {
        private val QR_TEXT = "QR_TEXT"
        private val QR_HEAD = "QR_HEAD"
        private val QR_SUBTITLE = "QR_SUBTITLE"
        private val QR_DESCRIPTION = "QR_DESCRIPTION"

        fun newIntent(
            qrText: String,
            qrHead: String?,
            qrSubtitle: String?,
            qrDescription: String?
        ): QrFragment = QrFragment().also {
            val bundle = Bundle()
            bundle.putString(QR_TEXT, qrText)
            if (qrHead != null) bundle.putString(QR_HEAD, qrHead)
            if (qrSubtitle != null) bundle.putString(QR_SUBTITLE, qrSubtitle)
            if (qrDescription != null) bundle.putString(QR_DESCRIPTION, qrDescription)
            it.arguments = bundle
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_popup_qr
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (qrText.isNotEmpty()) {
            binding.qrImage.setQRText(qrText)
        }
    }

    private lateinit var binding: FragmentPopupQrBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentPopupQrBinding.inflate(inflater)
        val bundle = this.arguments
        if (bundle != null) {
            qrText = bundle.getString(QR_TEXT, "")
            qrHead = bundle.getString(QR_HEAD, "")
            qrSubtitle = bundle.getString(QR_SUBTITLE, "")
            qrDescription = bundle.getString(QR_DESCRIPTION, "")
        }

        return binding.root

    }

    override fun initUI() {
        if (qrHead.isNotBlank()) binding.head.text = qrHead
        if (qrSubtitle.isNotBlank()) binding.subtitle.text = qrSubtitle
        if (qrDescription.isNotBlank()) binding.description.text = qrDescription
    }
}

