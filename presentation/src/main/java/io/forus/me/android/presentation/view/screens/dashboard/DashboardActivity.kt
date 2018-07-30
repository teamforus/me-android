package io.forus.me.android.presentation.view.screens.dashboard

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.ToolbarListener

import io.forus.me.android.presentation.view.activity.ToolbarActivity
import io.forus.me.android.presentation.view.adapters.MainViewPagerAdapter
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.property.PropertyFragment
import io.forus.me.android.presentation.view.screens.records.categories.RecordCategoriesFragment
import io.forus.me.android.presentation.view.screens.records.list.RecordsFragment
import kotlinx.android.synthetic.main.dashboard_activity.*
import java.util.logging.Handler


class DashboardActivity : ToolbarActivity() {

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

        bottom_navigation.setOnTabSelectedListener { position, wasSelected -> showTab(position, wasSelected) }
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
            selectTab(currentPagerPosition)
        }



        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()


        (android.os.Handler()).postDelayed({
            showTab(0, false)
        },500)

    }

    private fun selectTab(currentPagerPosition: Int) {
        bottom_navigation.setCurrentItem(currentPagerPosition, false)
        try {
            showTab(currentPagerPosition, false)
        } catch (e: Exception) {

        }

    }

    override val toolbarTitle: String
        get() = getString(R.string.blank_string)


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return currentFragment?.onOptionsItemSelected(item) ?: false
    }


    private fun showTab(position: Int, wasSelected: Boolean): Boolean {
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

        if (currentFragment != null ) {
            val castFragment = currentFragment!!
            when (castFragment){
                is ToolbarListener -> initSubView(castFragment.subviewFragment, castFragment.pageTitle, castFragment.menu)

            }
        }

//        if (currentFragment != null)
//            currentFragment.willBeDisplayed()

       // sliding_tabs.setVisibility(if (currentFragment.hideTabLayout()) View.GONE else View.VISIBLE)

//        if (!currentFragment.initNewToolbar()) {
//            currentFragment.setToolbarTitle(toolbar)
//            // toolbar.setTitle("Ass");
//            supportActionBar.setTitle(toolbar.title)
//        }
//
//        if (currentFragment is TabLayoutHelper.TabLayoutListener) {
//            (currentFragment as TabLayoutHelper.TabLayoutListener).setTabLayout(tabs)
//            (currentFragment as TabLayoutHelper.TabLayoutListener).initViewPager()
//        } else {
//            tabs.setVisibility(View.GONE)
//        }


        return true
    }

    private fun initSubView(fragment: BaseFragment?, title: String, menu: Int?) {
        if (menu != null) {
            menuInflater.inflate(menu!!, this.menu);
        } else {
            menuInflater.inflate(R.menu.empty, this.menu)
        }
        setToolbarTitle(title)
        if (fragment != null) {

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.subview, fragment)
                    .commit()
        } else {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentById(R.id.subview))
                    .commit()
        }

    }




}
