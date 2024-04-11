package io.forus.me.android.presentation.view.screens.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.example.snackbarexample.customsnackbar.chef.UpdateAppSnackbar
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentDashboardBinding
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.adapters.MainViewPagerAdapter
import io.forus.me.android.presentation.view.screens.account.account.AccountFragment
import io.forus.me.android.presentation.view.screens.main.MainActivity
import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersFragment

private const val CURRENT_PAGER_POSITION = "CURRENT_PAGER_POSITION"

class DashboardFragment : Fragment() {

    private var navigationAdapter: AHBottomNavigationAdapter? = null

    private var currentFragment: Fragment? = null
    private var currentPagerPosition = 0

    private var adapter: MainViewPagerAdapter? = null

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentDashboardBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.bottomNavigation.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener {
            var lastPosition: Int = 0
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
                val result = showTab(position, lastPosition, wasSelected)

                if (result) this.lastPosition = position

                return result
            }
        })

        binding.bottomNavigation.setOnNavigationPositionListener { }
        binding.bottomNavigation.accentColor =
                (activity as? CommonActivity)?.getCustomColor(R.color.colorAccent) ?: 0


        navigationAdapter = AHBottomNavigationAdapter(activity, R.menu.bottom_navigation_menu)
        navigationAdapter?.setupWithBottomNavigation(binding.bottomNavigation)

        if (adapter == null) {

            val fragments = ArrayList<Fragment?>()
            val titles = ArrayList<String>()

            fragments.add(VouchersFragment.newIntent())
            titles.add("")
            fragments.add(Fragment())
            titles.add("")
            fragments.add(AccountFragment())
            titles.add("")

            adapter = MainViewPagerAdapter(childFragmentManager, activity?.applicationContext,
                    fragments, titles)

            binding.viewPager.offscreenPageLimit = 3
        }

        binding.viewPager.adapter = adapter
        selectTab(currentPagerPosition, 0)


        SharedPref.init(requireContext())
        val neesAppUpdate = SharedPref.read(SharedPref.OPTION_NEED_APP_UPDATE,false)
        if(neesAppUpdate) {
            h.postDelayed({
                UpdateAppSnackbar
                        .make(binding.coordinator, View.OnClickListener {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            requireContext().startActivity(intent)
                            requireActivity().finish()
                        })
                        .show()
            }, 3000)
        }

    }

    val h = Handler()

    private fun selectTab(currentPagerPosition: Int, oldPosition: Int) {
        binding.bottomNavigation.setCurrentItem(currentPagerPosition, false)
        try {
            showTab(currentPagerPosition, oldPosition, false)
        } catch (e: Exception) {

        }

    }

    private fun showTab(position: Int, oldPosition: Int, wasSelected: Boolean): Boolean {
        if (position == 1) {
            binding.viewPager.setCurrentItem(oldPosition, false)
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

        binding.viewPager.setCurrentItem(position, false)
        currentFragment = adapter?.currentFragment

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