package io.forus.me.views.wallet

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * Created by martijn.doornik on 28/03/2018.
 */
class WalletPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    companion object {
        val ASSETS_VIEW = 1
        val SERVICES_VIEW = 2
        val TOKENS_VIEW = 0
    }

}