package io.forus.me.android.presentation.view.component.buttons

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import io.forus.me.android.presentation.R
import android.view.LayoutInflater
import android.view.View
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.view.component.images.AutoLoadImageView
import android.view.Gravity
import android.widget.*
import android.widget.RelativeLayout
import android.R.attr.gravity
import android.util.TypedValue
import android.widget.LinearLayout
import io.forus.me.android.presentation.view.component.FontType


class ShareButton : FrameLayout {


    private lateinit var  mRootView : View

    private lateinit var  mContainer : LinearLayout

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
        mRootView = inflater.inflate(R.layout.view_share_button, this)
        mContainer = mRootView.findViewById(R.id.container);




    }



}
