package io.forus.me.views.wallet

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * Created by martijn.doornik on 30/03/2018.
 */
class WalletItemDetailPager : ViewPager {
    constructor(context:Context) : super (context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    companion object {
        val OVERVIEW_PAGE = 1
        val RECEIVE_PAGE = 2
        val SEND_PAGE = 0
    }
}