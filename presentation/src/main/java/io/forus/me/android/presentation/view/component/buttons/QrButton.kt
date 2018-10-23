package io.forus.me.android.presentation.view.component.buttons

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.FontCache
import io.forus.me.android.presentation.view.component.FontType
import kotlinx.android.synthetic.main.view_qr_button.view.*


class QrButton : FrameLayout {

    protected val layout: Int
        get() = R.layout.view_qr_button

    constructor(context: Context) : super(context) {
        initNonStyle(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initNonStyle(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun initNonStyle(context: Context,  attrs: AttributeSet?) {
        init(context, attrs)
    }

    private fun init(context: Context,  attrs: AttributeSet?) {
        val inflater = LayoutInflater.from(context)
        val mRootView = inflater.inflate(layout, this)
        val btn_qr = mRootView.findViewById<android.support.v7.widget.AppCompatButton>(R.id.qr_click)

        val fontType = FontType.Regular
        btn_qr.typeface = FontCache.getTypeface(fontType.getFontPath(), context)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        qr_click.setOnClickListener(l)
    }
}
