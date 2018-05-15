package io.forus.me.views.wallet

import io.forus.me.R
import io.forus.me.entities.Token
import io.forus.me.services.TokenService
import io.forus.me.services.Web3Service
import io.forus.me.web3.TokenContract
import io.forus.me.web3.base.Transaction

class TokenFragment : WalletPagerFragment<Token>(TokenService()) {

    override fun getLayoutResource(): Int {
        return R.layout.token_list_item_view
    }





}