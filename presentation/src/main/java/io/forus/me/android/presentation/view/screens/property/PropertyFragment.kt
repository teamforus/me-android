
package io.forus.me.android.presentation.view.screens.property

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.ToolbarListener
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
class PropertyFragment : BaseFragment(), ToolbarListener {

    private val randomTitle = Random().nextInt(1000).toString();
    private var tabLayout: TabLayoutFragment? = null

    companion object {
        fun newIntent(): PropertyFragment {
            return PropertyFragment()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.property_fragment
    }

    override val pageTitle: String
        get() = getString(R.string.property)

    override val menu: Int?
        get() = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


    }








    override fun initUI() {

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

