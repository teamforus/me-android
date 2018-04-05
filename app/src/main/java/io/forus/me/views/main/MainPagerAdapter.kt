package io.forus.me.views.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import io.forus.me.views.base.TitledFragment

/**
 * Created by martijn.doornik on 13/03/2018.
 */
class MainPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<TitledFragment>) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments[position].title
    }

    override fun getCount(): Int {
        return fragments.size
    }

}