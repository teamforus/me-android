package io.forus.me.android.presentation.view.component.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.view_share_button.view.*


class ShareButton : FrameLayout {

    var text: String? = ""

    var icon: Drawable? = null

    var color: Int? = null

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
        mContainer = mRootView.findViewById(R.id.container)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomShareButtonAttrs, 0, 0)

        if (ta.hasValue(R.styleable.CustomShareButtonAttrs_sb_text)) {
            val str = ta.getString(R.styleable.CustomShareButtonAttrs_sb_text)
            if (!str.isNullOrEmpty())
                this.text = str
        }

        if (ta.hasValue(R.styleable.CustomShareButtonAttrs_sb_icon)) {
            val drawable = ta.getDrawable(R.styleable.CustomShareButtonAttrs_sb_icon)
            if (drawable != null)
                this.icon = drawable
        }

        if (ta.hasValue(R.styleable.CustomShareButtonAttrs_sb_icon_tint)) {
            val color = ta.getColor(R.styleable.CustomShareButtonAttrs_sb_icon_tint, 0)
            if (color != 0)
                this.color = color
        }

        val imageView : ImageView = mContainer.findViewById(R.id.iv_icon)
        if(icon != null) {
            imageView.setImageDrawable(icon)
            if(color != null){
                imageView.setColorFilter(color!!)
            }
        }
        val textView : io.forus.me.android.presentation.view.component.text.TextView = mContainer.findViewById(R.id.tv_text)
        textView.text = text

        ta.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        container.setOnClickListener(l)
    }
}
