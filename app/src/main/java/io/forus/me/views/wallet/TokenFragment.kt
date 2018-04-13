package io.forus.me.views.wallet

import android.util.Log
import io.forus.me.R
import io.forus.me.entities.Token
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.TokenService
import io.forus.me.services.Web3Service
import io.forus.me.web3.TokenContract
import io.forus.me.web3.base.UpdateEvent
import rx.Subscriber

class TokenFragment : WalletPagerFragment<Token>() {

    override fun getLayoutResource(): Int {
        return R.layout.token_list_item_view
    }

    override fun onItemAttach(newItem: Token) {
        val contract = TokenContract(newItem.address)
        contract.getTransferObservable(newItem).subscribe(WalletListAdapter.UpdateSubscriber(this, newItem))
    }

    override fun onItemDetach(removedItem: Token) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemUpdate(updateEvent: UpdateEvent, updatedItem: Token) {
        val contract = TokenContract(updatedItem.address)
        updatedItem.value = contract.getBalance(Web3Service.account!!).send().value.toFloat()
        updatedItem.lastSync = updateEvent.blockNumber
        TokenService.updateToken(updatedItem)
        this.view?.post({
            this.adapter.notifyDataSetChanged()
        })
    }





}