package io.forus.me.android.presentation.view.component.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import net.glxn.qrgen.android.QRCode
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import io.forus.me.android.presentation.R

class QRCodeImageView : AutoLoadImageView {

    var bitmapSize = 400
    var onColor = Color.WHITE
    var offColor = Color.BLACK

    constructor(context: Context) : super(context) { initUI(context, null) }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { initUI(context, attrs) }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) { initUI(context, attrs) }

    private fun initUI(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.QRCodeImageView, 0, 0)
        onColor = ta.getColor(R.styleable.QRCodeImageView_onColor, onColor)
        offColor = ta.getColor(R.styleable.QRCodeImageView_offColor, offColor)
        ta.recycle()
    }

    fun setQRText(text: String) {
        this.setImageBitmap(getQrBitmap(text,
                bitmapSize,
                onColor,
                offColor))
    }



    private fun getQrBitmap(text:String, size:Int, onColor: Int = Color.BLACK, offColor:Int = Color.WHITE): Bitmap {





        var bitmap = QRCode.from(text)
                //.withHint(EncodeHintType.MARGIN, 0)
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
