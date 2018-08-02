package io.forus.me.android.data.repository.wallets

import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable
import io.reactivex.Single

class WalletsRepository : io.forus.me.android.domain.repository.wallets.WalletsRepository {

    override fun getWallets(): Observable<List<Wallet>> {
        val wallets : MutableList<Wallet> = mutableListOf()

      //  for (i in 1..100){

        wallets.add(Wallet("Ethereum", 2.568396f, Currency("Eth", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/512px-Ethereum_logo_2014.svg.png"),
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Ethereum_logo_2014.svg/512px-Ethereum_logo_2014.svg.png"))
        wallets.add(Wallet("Basic Attention Token", 67843.767f, Currency("Bat", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Basic_Attention_Token_Icon.svg/512px-Basic_Attention_Token_Icon.svg.png"),
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Basic_Attention_Token_Icon.svg/512px-Basic_Attention_Token_Icon.svg.png"))
        wallets.add(Wallet("ERC-20 Token", 45.12f, Currency("ERC-20", "http://bestgpuformining.com/wp-content/uploads/2017/09/ERC20.png"),
                "http://bestgpuformining.com/wp-content/uploads/2017/09/ERC20.png"))

     //   }

        return Single.just(wallets).toObservable().
                map {
                    it.toList()
                }
    }
}