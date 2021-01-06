package io.forus.me.android.presentation.view.component.buttons

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import io.forus.me.android.presentation.R
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.helpers.FontCache
import io.forus.me.android.presentation.view.component.FontType


class ButtonNext : android.support.v7.widget.AppCompatButton {

    private var reverse : Boolean = false
    private var customTextSize: Float = 16f

    var active : Boolean = true
        set(value) {
            field = value
            initFont()
            initBackground()
        }

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
        this.minimumHeight = Converter.convertDpToPixel(40f, context)

        if (attrs != null) {

            val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomButtonAttrs, 0, 0)

            if (ta.hasValue(R.styleable.CustomButtonAttrs_reverse)) {
                reverse = ta.getBoolean(R.styleable.CustomButtonAttrs_reverse, false)

            }
            if (ta.hasValue(R.styleable.CustomButtonAttrs_textSize)) {
                customTextSize = ta.getFloat(R.styleable.CustomButtonAttrs_textSize, 16f)
            }
            ta.recycle()
        }

        initFont()
        initBackground()
    }

    private fun initFont(){
        setTextColor(if (!active) ContextCompat.getColor(context, R.color.body_1_38) else (if (reverse) ContextCompat.getColor(context, R.color.colorAccent) else ContextCompat.getColor(context, R.color.colorAccent)))
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, customTextSize)
        val fontType = FontType.Regular


       // typeface = FontCache.getTypeface(fontType.getFontPath(), context)

        this.typeface = ResourcesCompat.getFont(context, R.font.google_sans_medium)
    }

    private fun initBackground(){
        setBackgroundResource(if (!reverse && active )  R.drawable.button_main_round_blue else R.drawable.button_main_raund_reverse)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.stateListAnimator = null
        }

    }
}
