package io.forus.me.android.domain.repository.vouchers

import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest
import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.domain.models.common.Page
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable

interface VouchersRepository {

    fun getVouchers(): Observable<List<Voucher>>


    fun getVoucher(id: String): Observable<Voucher>

    fun getTransactions(voucherId: String): Observable<Page<Transaction>>
}
