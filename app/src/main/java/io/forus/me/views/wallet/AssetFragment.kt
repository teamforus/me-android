package io.forus.me.views.wallet

import io.forus.me.R
import io.forus.me.entities.Asset
import io.forus.me.services.AssetService

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class AssetFragment : WalletPagerFragment<Asset>(AssetService()) {
    override fun getLayoutResource(): Int {
        return R.layout.asset_list_item_view
    }
}