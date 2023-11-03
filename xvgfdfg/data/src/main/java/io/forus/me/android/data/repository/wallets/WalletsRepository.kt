package io.forus.me.android.data.repository.wallets

import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.wallets.Transaction
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

class WalletsRepository : io.forus.me.android.domain.repository.wallets.WalletsRepository {

    val wallets : MutableList<Wallet> = mutableListOf()
    val transactions : MutableList<Transaction> = mutableListOf()

    init {
        val eth = Currency("Eth", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/512px-Ethereum_logo_2014.svg.png")
        val bat = Currency("Bat", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Basic_Attention_Token_Icon.svg/512px-Basic_Attention_Token_Icon.svg.png")
        val erc = Currency("ERC-20", "http://bestgpuformining.com/wp-content/uploads/2017/09/ERC20.png")

        wallets.add(Wallet(0, "Ethereum","0xa970fA71A4AC7A3def1E93a7FefD4638bB474F76", 2.568396f, eth,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/512px-Ethereum_logo_2014.svg.png"))
        wallets.add(Wallet(1, "Basic Attention Token", "0xa970fA71A4AC7A3def1E93a7FefD4638bB474F77",67843.767f, bat,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Basic_Attention_Token_Icon.svg/512px-Basic_Attention_Token_Icon.svg.png"))
        wallets.add(Wallet(2, "ERC-20 Token", "0xa970fA71A4AC7A3def1E93a7FefD4638bB474F78", 45.12f, erc,
                "http://bestgpuformining.com/wp-content/uploads/2017/09/ERC20.png"))

        for(i in 0L..2L){
            transactions.add(Transaction("0xa970fA71A4AC7A3def1E93a7FefD4638bB474F76", i, Date(), "Kindpakket 2018", "Lidl Zuidhorn", wallets.get(i.toInt()).currency!!, (-2.100532856125252f).toDouble(), Transaction.Type.INCOME, Transaction.Status.IN_PROGRESS))
            transactions.add(Transaction("0xa970fA71A4AC7A3def1E93a7FefD4638bB474F76", i, Date(), "Kindpakket 2018", "Lidl Zuidhorn", wallets.get(i.toInt()).currency!!, (78.986783409).toDouble(), Transaction.Type.PAYMENT, Transaction.Status.CONFIRMED))
            transactions.add(Transaction("0xa970fA71A4AC7A3def1E93a7FefD4638bB474F76", i, Date(), "Kindpakket 2018", "Lidl Zuidhorn", wallets.get(i.toInt()).currency!!, (78.986783409).toDouble(), Transaction.Type.PAYMENT, Transaction.Status.CANCELED))
        }
    }

    override fun getWallets(): Observable<List<Wallet>> {
        return Single.just(wallets).toObservable().
                map {
                    it.toList()
                }
                .delay(300, TimeUnit.MILLISECONDS)
    }

    override fun getWallet(walletId: Long): Observable<Wallet> {
        val item: Wallet? = wallets.find{it -> it.id == walletId}
        if(item != null) return Single.just(item).toObservable().delay(300, TimeUnit.MILLISECONDS)
        else return Observable.error(Exception("Not found"))
    }

    override fun getTransactions(walletId: Long): Observable<List<Transaction>> {
        return Single.just(transactions.filter { it.walletId == walletId }).toObservable().
                map {
                    it.toList()
                }
                .delay(300, TimeUnit.MILLISECONDS)
    }

    override fun getTransaction(transactionId: String): Observable<Transaction> {
        val item: Transaction? = transactions.find{it -> it.id == transactionId}
        if(item != null) return Single.just(item).toObservable().delay(300, TimeUnit.MILLISECONDS)
        else return Observable.error(Exception("Not found"))
    }
}