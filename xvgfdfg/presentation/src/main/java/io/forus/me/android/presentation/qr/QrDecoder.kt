package io.forus.me.android.presentation.qr

import com.google.gson.GsonBuilder
import io.forus.me.android.domain.models.qr.QrCode

class QrDecoder{ 

    private val gson = GsonBuilder().create()

    fun decode(text: String): QrDecoderResult {

        return try {
            val qr: QrCode = gson.fromJson(text, QrCode::class.java)
            when(qr.type){
                QrCode.Type.AUTH_TOKEN -> QrDecoderResult.RestoreIdentity(qr.value)
                QrCode.Type.VOUCHER -> QrDecoderResult.ScanVoucher(qr.value)
                QrCode.Type.P2P_RECORD -> QrDecoderResult.ApproveValidation(qr.value)
                QrCode.Type.P2P_IDENTITY -> QrDecoderResult.UnknownQr(UnsupportedOperationException("Not implemented"))
                QrCode.Type.DEMO_VOUCHER -> QrDecoderResult.DemoVoucher(qr.value)
            }
        }
        catch (e: Exception){
            QrDecoderResult.UnknownQr(e)
        }
    }
}