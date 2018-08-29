package com.ocrv.ekasui.mrm.ui.views.card

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import io.forus.me.android.presentation.R


open class SettingsSwitchCard :  SettingsCard {

    var switcher: Switch? = null
        set(value) {
            if (field == null)
                field = value
        }
    override val layoutID: Int
        get() = R.layout.settings_title_value_card_switch

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)


    override fun prepareItems() {
        super.prepareItems()
        switcher = mRootView?.findViewById(R.id.switcher)

        vConteiner?.setOnClickListener {
            switcher?.isChecked = !(switcher?.isChecked ?: false)
        }
    }

    fun setChecked(isChecked: Boolean) {
        switcher?.isChecked = isChecked
    }
}





