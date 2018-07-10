package io.forus.me.android.data.repository.web3.datasource

import io.reactivex.Observable
import org.web3j.crypto.Credentials


interface Web3DataSource {
    fun getCredentials(): Observable<Credentials>


    fun createAccount(): Observable<Credentials>
}
