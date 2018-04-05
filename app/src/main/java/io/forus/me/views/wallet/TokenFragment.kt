package io.forus.me.views.wallet

import io.forus.me.R
import io.forus.me.entities.Token

class TokenFragment : WalletPagerFragment<Token>() {
    override fun getLayoutResource(): Int {
        return R.layout.token_list_item_view
    }

}