package io.forus.me.android.presentation.view.component.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.android.presentation.R

/**
 * Created by pavel on 27.10.17.
 */

open class SettingsCard : FrameLayout {

    protected var mRootView: View? = null
    protected var tvTitle: TextView? = null
    protected var tvText: TextView? = null
    protected var vDevider: View? = null
    protected var vConteiner: View? = null
    protected var iIcon: ImageView? = null


    internal var hideDevider: Boolean = false
    internal var title: String? = null
        set(value) {
            field = value
            initUI()
        }
    internal var icon: Int = 0
        set(value) {
            field = value
            initUI()
        }

    var text: String? = null
        set(value) {
            field = value
            initUI()
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TitleValueCard, 0, 0)
        title = ta.getString(R.styleable.TitleValueCard_valuecard_title)
        icon = ta.getResourceId(R.styleable.TitleValueCard_valuecard_icon, 0)
        text = ta.getString(R.styleable.TitleValueCard_valuecard_text)
        hideDevider = ta.getBoolean(R.styleable.TitleValueCard_valuecard_hidedevider, false)
        ta.recycle()

        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    open val layoutID: Int = R.layout.settings_title_value_card

    protected open fun init(context: Context) {

        val inflater = LayoutInflater.from(context)
        mRootView = inflater.inflate(layoutID, this)
        prepareItems()

        //DUMMY DATA
    }

    open fun prepareItems(){
        tvTitle = mRootView!!.findViewById(R.id.title)
        tvText = mRootView!!.findViewById(R.id.text)
        vDevider = mRootView!!.findViewById(R.id.devider)
        vConteiner = mRootView!!.findViewById(R.id.container)
        iIcon =  mRootView!!.findViewById(R.id.icon)


        initUI()
    }



    internal fun initUI() {
        tvTitle?.text = title
        tvText?.text = text

        if (icon > 0){
            iIcon?.setImageResource(icon)
        }

        vDevider?.visibility = if (!hideDevider) View.VISIBLE else View.GONE
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        vConteiner?.visibility = visibility
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (id == R.id.privacy_policy_card) {
            vConteiner?.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.profile_privacy_policy_url)))
                context.startActivity(intent)
            }
        } else {
            vConteiner?.setOnClickListener {
                l?.onClick(it)
            }
        }
    }





}


