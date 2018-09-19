package io.forus.me.android.presentation.view.component.text

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import io.forus.me.android.presentation.R
import android.graphics.Typeface
import io.forus.me.android.presentation.helpers.FontCache
import io.forus.me.android.presentation.view.component.FontType


class TextView : AppCompatTextView {


    var type: FontType = FontType.Regular

    constructor(context: Context) : super(context) {
        initUI(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initUI(context, attrs)

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initUI(context, attrs)
    }

    private fun initUI(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextViewAttrs, 0, 0)

        if (ta.hasValue(R.styleable.CustomTextViewAttrs_type)) {
            val stringtype = ta.getString(R.styleable.CustomTextViewAttrs_type)
            if (!stringtype.isNullOrEmpty())
                type = FontType.getFromString(stringtype.toLowerCase(), type)
        }
        ta.recycle()


        initType(context)
    }





    private fun initType(context: Context) {


        this.typeface = FontCache.getTypeface(type.getFontPath(), context)
    }




}
