package io.forus.me.android.presentation.view.component.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import net.glxn.qrgen.android.QRCode
import android.widget.ImageView
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

class QRCodeImageView : AutoLoadImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}


    fun setQRText(text: String) {
        this.setImageBitmap(getQrBitmap(text,
                this.width,
                Color.WHITE,
                Color.BLACK))
    }



    private fun getQrBitmap(text:String, size:Int, onColor: Int = Color.BLACK, offColor:Int = Color.WHITE): Bitmap {





        var bitmap = QRCode.from(text)
                .withHint(EncodeHintType.MARGIN, 0)
                .withColor(onColor, offColor)
                .withErrorCorrection(ErrorCorrectionLevel.H)
                .withSize(size, size).bitmap()

        var margin = 4
        if (size > 200) {
            margin = 6
        }
        bitmap = Bitmap.createBitmap(
                bitmap,
                margin,
                margin,
                bitmap.width - 2*margin,
                bitmap.height - 2*margin)
        return bitmap
    }
}
