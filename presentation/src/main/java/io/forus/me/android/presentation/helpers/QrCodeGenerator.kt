package io.forus.me.android.presentation.helpers

import android.graphics.Bitmap
import io.reactivex.Single
import net.glxn.qrgen.android.QRCode


object QrCodeGenerator {

    fun getRecordQrCode(uuid: String, width: Int, height: Int): Single<Bitmap> {
        return Single.fromCallable {
            QRCode.from(uuid).withSize(width, height).bitmap()
        }
    }
}