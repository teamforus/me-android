package io.forus.me.android.data.repository.web3.datasource.local

import android.content.Context
import io.forus.me.android.data.repository.web3.datasource.Web3DataSource
import io.reactivex.Observable
import org.bouncycastle.util.encoders.Hex
import org.web3j.crypto.Credentials
import java.io.File
import java.util.*


class Web3LocalDataSource(val context: Context) : Web3DataSource {


    private val directoryPrefix = context.filesDir.toString()
    private val wallet_location = "/wallet/%S/"
    private val keyName = ".key"


    private val credentials : Credentials by lazy {
        val file = File(walletFileDirectory, keyName)
        return@lazy Credentials.create(Hex.toHexString(file.readBytes()))
    }



    override fun getCredentials(): Observable<Credentials> {
        return Observable.just(credentials)
    }

    override fun createAccount(): Observable<Credentials> {
        val bytes = ByteArray(32)
        Random().nextBytes(bytes)
        val credentials = Credentials.create(Hex.toHexString(bytes))
        val file = File(walletFileDirectory, keyName)
        if (!file.exists()) {
            file.createNewFile()
        } else {
            // TODO DEBUG throw Exception("Keypair already exists on device!")
        }
        file.writeBytes(credentials.ecKeyPair.privateKey.toByteArray())
        return getCredentials()
    }



    private fun walletLocation(network: String): String {
        return directoryPrefix + String.format(wallet_location, network)
    }

    private val walletFileDirectory:String
        get() {
            val ret = walletLocation("???")
            val dir = File(ret)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return ret
        }


}
