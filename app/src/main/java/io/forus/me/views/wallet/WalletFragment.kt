package io.forus.me.views.wallet

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.R
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.forus.me.entities.Asset
import io.forus.me.entities.Service
import io.forus.me.entities.Token
import io.forus.me.views.base.TitledFragment

class WalletFragment : TitledFragment() {

    private lateinit var assetFragment: AssetFragment
    private lateinit var navigation:TabLayout
    private lateinit var pager:WalletPager
    private lateinit var serviceFragment: ServiceFragment
    private lateinit var tokenFragment: TokenFragment

    fun init(
            tokens: List<Token>,
            assets: List<Asset>,
            services: List<Service>
    ) {
        this.setTokens(tokens)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_fragment, container, false)
        assetFragment = AssetFragment().also { it.title = resources.getString(R.string.tab_assets) }
        serviceFragment = ServiceFragment().also { it.title = resources.getString(R.string.tab_services) }
        tokenFragment = TokenFragment().also { it.title = resources.getString(R.string.tab_tokens) }
        val pages = listOf<TitledFragment>(
                tokenFragment,
                assetFragment,
                serviceFragment
        )
        navigation = view.findViewById(R.id.navigation_wallet)
        pager = view.findViewById(R.id.wallet_pager)
        pager.adapter = TitledFragmentPagerAdapter(childFragmentManager, pages)
        navigation.setupWithViewPager(pager)
        return view
    }

    fun notifyTokensChanged() {
        tokenFragment.adapter.notifyDataSetChanged()
    }

    fun setAssets(assets: List<Asset>) {

    }

    fun setServices(services: List<Service>) {

    }

    fun setTokens(tokens: List<Token>) {
        tokenFragment.setItems(tokens)
    }

    fun showAssets() {
        this.pager.currentItem = 1
    }

    fun showServices() {
        this.pager.currentItem = 2
    }

    fun showTokens() {
        this.pager.currentItem = 0
    }
}