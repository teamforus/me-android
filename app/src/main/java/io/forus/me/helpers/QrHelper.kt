package io.forus.me.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.EncodeHintType
import net.glxn.qrgen.android.QRCode
import org.json.JSONObject


/**
 * Created by martijn.doornik on 21/03/2018.
 */
class QrHelper {

    companion object {

        private fun getQrCacheName(name:String, size:Int, onColor:Int, offColor:Int): String {
            return "qr-$name:s$size:p$onColor:n$offColor"
        }

        fun getQrBitmap(context: Context, text:String, size:Int, onColor: Int = Color.BLACK, offColor:Int = Color.WHITE): Bitmap {
            val name = getQrCacheName(text, size, onColor, offColor)
            var bitmap:Bitmap? = CacheHelper.getBitmapCache(context, name)
            if (bitmap == null) {
                bitmap = QRCode.from(text)
                        .withHint(EncodeHintType.MARGIN, 0)
                        .withColor(onColor, offColor)
                        //.withErrorCorrection(ErrorCorrectionLevel.H)
                        .withSize(size, size).bitmap()
                if (bitmap != null) {
                    CacheHelper.add(context, name, bitmap)
                }
            }
            return bitmap!!
        }

        fun getQrBitmap(context: Context, json:JSONObject, size:Int, onColor: Int = Color.BLACK, offColor:Int = Color.WHITE): Bitmap {
            return getQrBitmap(context, json.toString(0), size, onColor, offColor)
        }
    }

    abstract class Sizes {
        companion object {
            const val LARGE = 192
            const val SMALL = 64
        }
    }
}