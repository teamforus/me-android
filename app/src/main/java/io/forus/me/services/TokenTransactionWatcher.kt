package io.forus.me.services

import android.util.Log
import io.forus.me.entities.Token
import io.forus.me.services.base.WatcherService
import io.forus.me.web3.TokenContract
import io.forus.me.web3.base.Transaction
import rx.Observable

/**
 * Created by martijn.doornik on 01/05/2018.
 */
class TokenTransactionWatcher : WatcherService<Token, TokenContract.TransferEvent>("TokenTransactionWatcher", TokenService()) {
    override fun onAdded(item: Token): Observable<TokenContract.TransferEvent> {
        val contract = TokenContract(item.address)
        return contract.getTransferObservable(item)
    }

    override fun onChange(item: Token) {
        super.onChange(item)
        NotificationService.push(this.baseContext, "Nieuwe transactie", "Er is een nieuwe transactie geweest op ${item.name}.")
    }

    override fun onEventTrigger(event: TokenContract.TransferEvent) {
        NotificationService.push(this.baseContext, "New transaction", event.transactionHash)
    }

    override fun onRemoved(item: Token) {
        // end sync
    }
}