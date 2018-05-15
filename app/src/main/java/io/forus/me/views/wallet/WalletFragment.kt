package io.forus.me.views.wallet

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.R
import io.forus.me.entities.Asset
import io.forus.me.entities.Voucher
import io.forus.me.entities.Token
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.AssetService
import io.forus.me.services.IdentityService
import io.forus.me.services.VoucherService
import io.forus.me.services.TokenService
import io.forus.me.views.base.TitledFragment
import kotlin.collections.emptyList

class WalletFragment : TitledFragment() {

    private lateinit var assetFragment: AssetFragment
    private lateinit var navigation:TabLayout
    private lateinit var pager:WalletPager
    private lateinit var voucherFragment: VoucherFragment
    private lateinit var tokenFragment: TokenFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_fragment, container, false)
        assetFragment = AssetFragment().also { it.title = resources.getString(R.string.tab_assets) }
        voucherFragment = VoucherFragment().also { it.title = resources.getString(R.string.tab_vouchers) }
        tokenFragment = TokenFragment().also { it.title = resources.getString(R.string.tab_tokens) }
        val pages = listOf<TitledFragment>(
                tokenFragment,
                assetFragment,
                voucherFragment
        )
        navigation = view.findViewById(R.id.navigation_wallet)
        pager = view.findViewById(R.id.wallet_pager)
        pager.adapter = TitledFragmentPagerAdapter(childFragmentManager, pages)
        navigation.setupWithViewPager(pager)
        return view
    }

    override fun onResume() {
        super.onResume()
        this.assetFragment.notifyDataSetChanged()
        this.tokenFragment.notifyDataSetChanged()
        this.voucherFragment.notifyDataSetChanged()
    }

    fun showAssets(hasUpdate: Boolean = false) {
        if (hasUpdate) {
            this.assetFragment.notifyDataSetChanged()
        }
        this.pager.currentItem = 1
    }

    fun showVouchers(hasUpdate: Boolean = false) {
        if (hasUpdate) {
            this.voucherFragment.notifyDataSetChanged()
        }
        this.pager.currentItem = 2
    }

    fun showTokens(hasUpdate: Boolean = false) {
        if (hasUpdate) {
            this.tokenFragment.notifyDataSetChanged()
        }
        this.pager.currentItem = 0
    }
}