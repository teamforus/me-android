package io.forus.me.android.presentation.view.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import java.util.ArrayList





class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }




//    override fun saveState(): Parcelable? {
//        return null//super.saveState()
//    }
//

//    override fun destroyItem(viewPager: ViewGroup, position: Int, `object`: Any) {
//        viewPager.removeView(`object` as View)
//    }



//    override fun getItemId(position: Int): Long {
//        return mWallets.get(position).
//    }
//    private fun getFragmentTag(viewPagerId: Int, fragmentPosition: Int): String {
//        return "android:switcher:$viewPagerId:$fragmentPosition"
//    }



}
