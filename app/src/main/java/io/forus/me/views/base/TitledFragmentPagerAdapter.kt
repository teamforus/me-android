package io.forus.me.views.wallet

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.forus.me.views.base.TitledFragment


class TitledFragmentPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<TitledFragment>): FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return fragments.size
    }
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments[position].title
    }

}