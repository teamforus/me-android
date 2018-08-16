package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.adapters.MainViewPagerAdapter
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.property.PropertyFragment
import io.forus.me.android.presentation.view.screens.records.categories.RecordCategoriesFragment
import kotlinx.android.synthetic.main.dashboard_activity.*


class DashboardActivity : CommonActivity() {

    private var currentFragment: android.support.v4.app.Fragment? = null
    private var currentPagerPosition = 0
    private var menu: Menu? = null
    private var navigationAdapter: AHBottomNavigationAdapter? = null

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }

    private var adapter: MainViewPagerAdapter? = null

    override val viewID: Int
        get() = R.layout.dashboard_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()

    }


    private fun initUI(){

        bottom_navigation.setOnTabSelectedListener ( object: AHBottomNavigation.OnTabSelectedListener {
            var lastPosition: Int = 0;
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
               var result =   showTab(position, lastPosition, wasSelected)
               this.lastPosition = position

               return result
            }
        } )

    //    bottom_navigation.setOnTabSelectedListener { position, wasSelected -> showTab(position, wasSelected) }
        bottom_navigation.setOnNavigationPositionListener({ y ->  })
        bottom_navigation.accentColor = getCustomColor(R.color.colorAccent)


        navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu)
        navigationAdapter?.setupWithBottomNavigation(bottom_navigation)

        if (adapter == null) {

            val fragments = ArrayList<android.support.v4.app.Fragment>();
            val titles = ArrayList<String>();

            fragments.add(PropertyFragment.newIntent())
            titles.add("")
            fragments.add(PropertyFragment.newIntent())
            titles.add("")
            fragments.add(RecordCategoriesFragment.newIntent())
            titles.add("")

            adapter = MainViewPagerAdapter(supportFragmentManager, applicationContext, fragments, titles)
            view_pager.adapter = adapter

            view_pager.offscreenPageLimit = 5;
            selectTab(currentPagerPosition, 0)
        }





        (android.os.Handler()).postDelayed({
            showTab(0, 0,false)
        },500)

    }

    private fun selectTab(currentPagerPosition: Int, oldPosition: Int) {
        bottom_navigation.setCurrentItem(currentPagerPosition, false)
        try {
            showTab(currentPagerPosition, oldPosition, false)
        } catch (e: Exception) {

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return currentFragment?.onOptionsItemSelected(item) ?: false
    }


    private fun showTab(position: Int, oldPosition: Int, wasSelected: Boolean): Boolean {

        if (position == 1) {
            view_pager.setCurrentItem(oldPosition, false)
            this.navigator.navigateToQrScanner(this)
            return false

        }

        if (adapter == null)
            return false

        if (currentFragment == null) {
            currentFragment = adapter!!.currentFragment
        }

        if (currentFragment == null)
            return false

        if (wasSelected) {
            return true
        }


        view_pager.setCurrentItem(position, false)
        currentFragment = adapter?.currentFragment


        return true
    }

}
