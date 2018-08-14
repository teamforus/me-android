package io.forus.me.android.presentation.helpers

import android.graphics.Bitmap
import io.reactivex.Single
import net.glxn.qrgen.android.QRCode
import java.util.concurrent.TimeUnit


object QrCodeGenerator {

    fun getRecordQrCode(uuid: String, width: Int, height: Int): Single<Bitmap> {
        return Single.fromCallable {
            QRCode.from(uuid).withSize(width, height).withColor(0xFF000000.toInt(), 0xFFF7F7F7.toInt()).bitmap()
        }.delay(400, TimeUnit.MILLISECONDS)
    }
}