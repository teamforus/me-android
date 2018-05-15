package io.forus.me.helpers

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

        fun getQrBitmap(text:String, size:Int, onColor: Int = Color.BLACK, offColor:Int = Color.WHITE): Bitmap {

            /*val result = MultiFormatWriter().encode(text,
                        BarcodeFormat.QR_CODE, size, size)
            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result.get(x, y)) onColor else offColor
                }
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, size, 0, 0, w, h)
            return bitmap*/



            var bitmap = QRCode.from(text)
                    .withHint(EncodeHintType.MARGIN, 0)
                    .withColor(onColor, offColor)
                    //.withErrorCorrection(ErrorCorrectionLevel.H)
                    .withSize(size, size).bitmap()

            /*var margin = 4
            if (size > 200) {
                margin = 6
            }
            bitmap = Bitmap.createBitmap(
                    bitmap,
                    margin,
                    margin,
                    bitmap.width - 2*margin,
                    bitmap.height - 2*margin)*/
            return bitmap
        }

        fun getQrBitmap(json:JSONObject, size:Int, onColor: Int = Color.BLACK, offColor:Int = Color.WHITE): Bitmap {
            return getQrBitmap(json.toString(0), size, onColor, offColor)
        }
    }
}