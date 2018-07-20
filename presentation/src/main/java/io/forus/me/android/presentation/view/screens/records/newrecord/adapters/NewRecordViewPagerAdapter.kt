package io.forus.me.android.presentation.view.screens.records.newrecord.adapters

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R


class NewRecordViewPagerAdapter : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {

        var resId = 0
        when (position) {
            0 -> resId = R.id.page_one
            1 -> resId = R.id.page_two
            2 -> resId = R.id.page_tree
        }
        return collection.findViewById(resId)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // No super
    }
}
