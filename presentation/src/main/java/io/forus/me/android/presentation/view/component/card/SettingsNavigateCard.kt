package io.forus.me.android.presentation.view.component.card

import android.content.Context
import android.util.AttributeSet
import android.widget.Switch
import io.forus.me.android.presentation.R


open class SettingsNavigateCard : SettingsCard {

    var switcher: Switch? = null
        set(value) {
            if (field == null)
                field = value
        }
    override val layoutID: Int
        get() = R.layout.settings_title_value_card_navigate

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





