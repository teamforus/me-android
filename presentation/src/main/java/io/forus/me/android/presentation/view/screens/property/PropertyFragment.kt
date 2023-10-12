
package io.forus.me.android.presentation.view.screens.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentFingerprintBinding
import io.forus.me.android.presentation.databinding.FragmentPropertyBinding
import io.forus.me.android.presentation.view.adapters.MainViewPagerAdapter
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.assets.AssetsFragment
import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersFragment
import io.forus.me.android.presentation.view.screens.wallets.WalletsFragment
import java.util.*


/**
 * Fragment Welcome Screen.
 */
class PropertyFragment : BaseFragment() {

    companion object {
        fun newIntent(): PropertyFragment {
            return PropertyFragment()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.fragment_property
    }

    override val allowBack: Boolean
        get() = false

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_property)


    val menu: Int?
        get() = null

    private lateinit var binding: FragmentPropertyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun initUI() {

        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        fragments.add(WalletsFragment.newIntent())
        titles.add(getString(R.string.dashboard_currency))

        fragments.add(AssetsFragment.newIntent())
        titles.add(getString(R.string.dashboard_assets))

        fragments.add(VouchersFragment.newIntent())
        titles.add(getString(R.string.dashboard_vouchers))


        val adapter = MainViewPagerAdapter(childFragmentManager, activity?.applicationContext, fragments, titles)
        binding.viewpager.adapter = adapter
        binding.viewpager.offscreenPageLimit = 3
        binding.slidingTabs.setupWithViewPager(binding.viewpager)

        super.initUI()
    }
}

