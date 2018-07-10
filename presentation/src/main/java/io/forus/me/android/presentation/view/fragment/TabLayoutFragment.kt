
package io.forus.me.android.presentation.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.adapters.ViewPagerAdapter
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.account.assigndelegates.AssignDelegatesAccountActivity
import kotlinx.android.synthetic.main.property_fragment.*
import kotlinx.android.synthetic.main.tab_layout.*
import kotlinx.android.synthetic.main.welcome_fragment.*
import java.util.*

/**
 * Fragment Welcome Screen.
 */
class TabLayoutFragment : BaseFragment() {

    private val randomTitle = Random().nextInt(1000).toString();

    private var viewPager: ViewPager? = null

    companion object {
        fun newIntent(): TabLayoutFragment {
            return TabLayoutFragment()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.tab_layout
    }

    override fun getTitle(): String {
        return  randomTitle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


    }





    fun setupWithViewPager(viewPager: ViewPager){
        this.viewPager = viewPager;


    }




    override fun initUI() {
        sliding_tabs.setupWithViewPager(viewPager)
    }
}

