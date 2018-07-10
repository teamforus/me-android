package io.forus.me.android.domain.repository.account

import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest
import io.reactivex.Observable

interface AccountRepository {

    fun requestDelegatesQRAddress(): Observable<RequestDelegatesQrModel>


    fun newUser(model: NewAccountRequest) : Observable<NewAccountRequest>


    fun loginByEmail(email: String) : Observable<RestoreAccountByEmailRequest>


    fun getLoginPin() : Observable<RequestDelegatesPinModel>

}
