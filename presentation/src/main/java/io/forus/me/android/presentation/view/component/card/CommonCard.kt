package io.forus.me.android.presentation.view.component.card

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import io.forus.me.android.presentation.R


class CommonCard : CardView {


    constructor(context: Context) : super(context) {
        initNonStyle(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initNonStyle(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun initNonStyle(context: Context, attrs: AttributeSet?) {

        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = 4f
        }

        this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
    }


}
