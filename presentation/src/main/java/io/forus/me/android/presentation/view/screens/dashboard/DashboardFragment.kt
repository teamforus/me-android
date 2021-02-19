package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.example.snackbarexample.customsnackbar.chef.UpdateAppSnackbar
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.adapters.MainViewPagerAdapter
import io.forus.me.android.presentation.view.screens.account.account.AccountFragment
import io.forus.me.android.presentation.view.screens.main.MainActivity
import io.forus.me.android.presentation.view.screens.records.categories.RecordCategoriesFragment
import io.forus.me.android.presentation.view.screens.records.list.RecordsFragment
import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*

private const val CURRENT_PAGER_POSITION = "CURRENT_PAGER_POSITION"

class DashboardFragment : Fragment() {

    private var navigationAdapter: AHBottomNavigationAdapter? = null

    private var currentFragment: android.support.v4.app.Fragment? = null
    private var currentPagerPosition = 0

    private var adapter: MainViewPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        bottom_navigation.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener {
            var lastPosition: Int = 0
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
                val result = showTab(position, lastPosition, wasSelected)

                if (result) this.lastPosition = position

                return result
            }
        })

        //    bottom_navigation.setOnTabSelectedListener { position, wasSelected -> showTab(position, wasSelected) }
        bottom_navigation.setOnNavigationPositionListener { }
        bottom_navigation.accentColor =
                (activity as? CommonActivity)?.getCustomColor(R.color.colorAccent) ?: 0


        navigationAdapter = AHBottomNavigationAdapter(activity, R.menu.bottom_navigation_menu)
        navigationAdapter?.setupWithBottomNavigation(bottom_navigation)

        if (adapter == null) {

            val fragments = ArrayList<Fragment?>()
            val titles = ArrayList<String>()

            //fragments.add(PropertyFragment.newInstance())
            fragments.add(VouchersFragment.newIntent())
            titles.add("")
            fragments.add(Fragment())
            titles.add("")
            fragments.add(RecordsFragment())
            //fragments.add(AccountFragment())
            titles.add("")

            adapter = MainViewPagerAdapter(childFragmentManager, activity?.applicationContext,
                    fragments, titles)

            view_pager.offscreenPageLimit = 3
        }

        view_pager.adapter = adapter
        selectTab(currentPagerPosition, 0)


        SharedPref.init(context!!)
        val neesAppUpdate = SharedPref.read(SharedPref.OPTION_NEED_APP_UPDATE,false)
        if(neesAppUpdate) {
            h.postDelayed({
                UpdateAppSnackbar
                        .make(coordinator, View.OnClickListener {
                            val intent = Intent(context!!, MainActivity::class.java)
                            context!!.startActivity(intent)
                            activity!!.finish()
                        })
                        .show()
            }, 3000)
        }

    }

    val h = Handler()

    private fun selectTab(currentPagerPosition: Int, oldPosition: Int) {

        bottom_navigation.setCurrentItem(currentPagerPosition, false)
        try {
            showTab(currentPagerPosition, oldPosition, false)
        } catch (e: Exception) {

        }

    }

    private fun showTab(position: Int, oldPosition: Int, wasSelected: Boolean): Boolean {
        Log.d("RECORDS","SELECT currentPagerPosition $position")
        if (position == 1) {
            view_pager.setCurrentItem(oldPosition, false)
            (activity as? DashboardActivity)?.navigateToQrScanner()

            return false

        }

        if (adapter == null)
            return false

        if (currentFragment == null) {
            currentFragment = adapter?.currentFragment
        }

        if (currentFragment == null)
            return false

        if (wasSelected) {
            return true
        }

        view_pager.setCurrentItem(position, false)
        currentFragment = adapter?.currentFragment

        if (position == 2) {
            (currentFragment as? RecordsFragment)?.showTooptip()
        }else{
            (adapter?.getItem(2) as? RecordsFragment)?.hideTooltip()
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_PAGER_POSITION, currentPagerPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            currentPagerPosition = it.getInt(CURRENT_PAGER_POSITION)
        }
    }
}