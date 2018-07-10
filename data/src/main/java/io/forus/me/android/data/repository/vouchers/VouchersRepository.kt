package io.forus.me.android.data.repository.vouchers

import io.forus.me.android.domain.models.common.Page
import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Voucher
import io.reactivex.Observable
import io.reactivex.Single

class VouchersRepository : io.forus.me.android.domain.repository.vouchers.VouchersRepository {

    private val vouchers: MutableList<Voucher> by lazy {
        val vouchers : MutableList<Voucher> = mutableListOf()

        for (i in 1..100){
            vouchers.add(Voucher(i.toString(), "Kindpacket", 2, Currency("€", "https://cdn.worldvectorlogo.com/logos/ethereum.svg"), 400f, "https://www.freelogodesign.org/Content/img/logo-ex-7.png"))
            vouchers.add(Voucher("__${i.toString()}", "Kindpacket 2", 3, Currency("€", "https://cdn.worldvectorlogo.com/logos/ethereum.svg"), 35.555f, "https://www.freelogodesign.org/Content/img/logo-ex-7.png"))

        }
        return@lazy vouchers
    }

    override fun getVoucher(id: String): Observable<Voucher> {

        return Single.just(vouchers[0]).toObservable()

    }

    override fun getVouchers(): Observable<List<Voucher>> {


        return Single.just(vouchers).toObservable().
                map {
                    it.toList()
                }
    }

    override fun getTransactions(voucherId: String): Observable<Page<Transaction>> {
        val items : MutableList<Transaction> = mutableListOf()

        for (i in 1..5){
            items.add(Transaction(i.toString(), "RMinds", Transaction.Type.Payed, Currency("BTC"), i.toDouble()))

        }
        return Single.just(Page(items)).toObservable()
    }

}