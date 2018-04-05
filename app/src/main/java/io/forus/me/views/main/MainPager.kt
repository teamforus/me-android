package io.forus.me.views.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import io.forus.me.views.me.MeFragment

/**
 * Created by martijn.doornik on 28/03/2018.
 */
class MainPager : ViewPager {
    constructor(context:Context) : super(context)
    constructor(context:Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    companion object {
        internal val WALLET_VIEW: Int = 0
        internal val ME_VIEW: Int = 1
        internal val RECORDS_VIEW: Int = 2
    }
}