package io.forus.me.android.presentation.view.component.pin

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


class PinView : FrameLayout {


    private lateinit var  mRootView : View

    private lateinit var  mContainer : LinearLayout

    private  var pin : String = ""

    private val tvList: MutableList<TextView> = mutableListOf()

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
        mRootView = inflater.inflate(R.layout.view_pin, this)
        mContainer = mRootView.findViewById(R.id.container)



        for (i in 1..5) {

            addTextView(true)
        }

        addTextView(false)

        setPin(pin)


    }

    fun setPin(pin: String){
        var i = 0
        this.pin = pin
        for (item in pin) {
            if (tvList.size > i)
                tvList[i].text = item.toString()
            i++

        }
    }


    private fun addTextView(showdot: Boolean){
        val lparams = LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        lparams.setMargins(Converter.convertDpToPixel(10f, context), 0, Converter.convertDpToPixel(10f, context), 0)

        //      lparams.gravity = Gravity.CENTER_VERTICAL

        val tv = io.forus.me.android.presentation.view.component.text.TextView(context)
        tv.layoutParams = lparams
        tv.setTextColor(ContextCompat.getColor(context, R.color.textColor))
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 58.2f)
        tv.text = ""
        tv.type = FontType.Medium
        mContainer.addView(tv)
        tvList.add(tv)

        if (showdot)
            drawDots()

    }

    private fun drawDots(){

        val parent = LinearLayout(context)

        parent.gravity = Gravity.CENTER_VERTICAL
        parent.layoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
        parent.orientation = LinearLayout.VERTICAL

//children of parent linearlayout

        val iv = ImageView(context)
        iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.grey_circle))
        iv

        parent.addView(iv)

        mContainer.addView(parent)
    }



}
