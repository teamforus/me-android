
package io.forus.me.android.presentation.view.screens.property

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.adapters.MainViewPagerAdapter
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.fragment.TabLayoutFragment
import io.forus.me.android.presentation.view.screens.assets.AssetsFragment
import kotlinx.android.synthetic.main.property_fragment.*
import java.util.*
import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersFragment
import io.forus.me.android.presentation.view.screens.wallets.WalletsFragment


/**
 * Fragment Welcome Screen.
 */
class PropertyFragment : BaseFragment() {

    private val randomTitle = Random().nextInt(1000).toString()
    private var tabLayout: TabLayoutFragment? = null

    companion object {
        fun newIntent(): PropertyFragment {
            return PropertyFragment()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.property_fragment
    }

    override val allowBack: Boolean
        get() = false

    override val toolbarTitle: String
        get() = getString(R.string.property)


    val menu: Int?
        get() = null


    override fun initUI() {

        super.initUI()

        tabLayout = TabLayoutFragment.newIntent()

        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        fragments.add(WalletsFragment.newIntent())
        titles.add(getString(R.string.valuta))

        fragments.add(AssetsFragment.newIntent())
        titles.add(getString(R.string.bezit))

        fragments.add(VouchersFragment.newIntent())
        titles.add(getString(R.string.vouchers))


        val adapter = MainViewPagerAdapter(childFragmentManager, activity?.applicationContext, fragments, titles)
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 5;
        tabLayout?.setupWithViewPager(viewpager)
    }

    override val subviewFragment: BaseFragment?
        get() = tabLayout
}

