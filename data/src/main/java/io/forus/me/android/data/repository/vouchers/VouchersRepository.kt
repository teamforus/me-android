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

        vouchers.add(Voucher("1", "Kindpakket", 2, Currency("€", "https://cdn.worldvectorlogo.com/logos/ethereum.svg"), 400f, "https://freeiconshop.com/wp-content/uploads/edd/person-flat.png"))

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