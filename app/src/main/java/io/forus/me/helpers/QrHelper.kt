package io.forus.me.helpers

import android.graphics.Bitmap
import com.google.zxing.EncodeHintType
import net.glxn.qrgen.android.QRCode
import org.json.JSONObject

/**
 * Created by martijn.doornik on 21/03/2018.
 */
class QrHelper {

    companion object {
        fun getQrBitmap(address:String): Bitmap {
            return QRCode.from(address).withHint(EncodeHintType.MARGIN, "0").bitmap()
        }

        fun getQrBitmap(json:JSONObject): Bitmap {
            return QRCode.from(json.toString(0)).withHint(EncodeHintType.MARGIN, "0").bitmap()
        }
    }
}