package io.forus.me.android.presentation.view.component.card

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import io.forus.me.android.presentation.R
import androidx.core.content.ContextCompat
import android.util.TypedValue
import androidx.cardview.widget.CardView
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.helpers.FontCache
import io.forus.me.android.presentation.view.component.FontType


class CommonCard : CardView {



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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = 4f
        }

        this.setCardBackgroundColor(ContextCompat.getColor(context,R.color.card_background))
    }


}
