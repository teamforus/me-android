package io.forus.me.services

import android.content.Context
import android.util.Log
import io.forus.me.helpers.ThreadHelper
import org.spongycastle.util.encoders.Hex
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.crypto.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.*
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.tx.RawTransactionManager
import org.web3j.utils.Numeric
import rx.Observable
import rx.Subscriber
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * Created by martijn.doornik on 15/02/2018.
 */

internal class Web3Service {

    companion object {
        private var _credentials: Credentials? = null
        val instance:Web3j = Web3jFactory.build(HttpService(Configuration.httpConnectionString))

        private val walletFileDirectory:String
            get() {
                val ret = Configuration.walletLocation(Configuration.ME_NETWORK_LOCATION)
                val dir = File(ret)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                return ret
            }

        val account:String?
            get() {
                if (credentials != null) {
                    return credentials!!.address
                }
                return null
            }

        val credentials: Credentials?
                get() {
                    if (_credentials == null) {
                        val file = File(walletFileDirectory, ".key")
                        if (file.exists()) {
                            _credentials = Credentials.create(Hex.toHexString(file.readBytes()))
                            Log.d("Web3Service", "Loading configuration for account \"${this.account}\"")
                        }
                    }
                    return _credentials
                }

        fun deleteAccount() {
            val file = File(walletFileDirectory, ".key")
            if (file.exists()) {
                file.delete()
            }
        }

        fun getEther(): BigInteger? {
            if (credentials != null) {
                val address = credentials!!.address
                Log.d("Web3", "Getting ether balance from \"$address\"")
                val ret = ThreadHelper.await(Callable {
                    instance.ethGetBalance(credentials!!.address, DefaultBlockParameterName.LATEST).send()
                })
                if (ret != null) {
                    return ret.balance
                }
            }
            return null
        }

        fun getBytecodeOf(contract:String): String? {
            val ethGetCode = instance
                    .ethGetCode(contract, DefaultBlockParameterName.LATEST)
                    .send()
            if (ethGetCode.hasError()) {
                return null
            }
            return Numeric.cleanHexPrefix(ethGetCode.code)
        }

        fun initialize(context: Context) {
            Configuration._directoryPrefix = context.filesDir.toString()
        }

        fun isInitialized(): Boolean {
            try {
                return Configuration._directoryPrefix.isNotEmpty()
            } catch (e:Exception) {}
            return false
        }

        fun newAccount(): String {
            val bytes = ByteArray(32)
            Random().nextBytes(bytes)
            val credentials = Credentials.create(Hex.toHexString(bytes))
            val file = File(walletFileDirectory, ".key")
            if (!file.exists()) {
                file.createNewFile()
            } else {
                // TODO DEBUG throw Exception("Keypair already exists on device!")
            }
            file.writeBytes(credentials.ecKeyPair.privateKey.toByteArray())
            _credentials = Credentials.create(Hex.toHexString(file.readBytes()))
            return credentials.address
        }

        fun sendEther(to: String, amount: Long): Boolean {
            val ethGetTransactionCount = instance.ethGetTransactionCount(
             this.account!!, DefaultBlockParameterName.LATEST).sendAsync().get()
            val nonce = ethGetTransactionCount.transactionCount
            val transaction = RawTransaction.createEtherTransaction(
                    nonce, Configuration.gasPrice, Configuration.gasLimit, to, amount.toBigInteger()
            )
            val encoded = Numeric.toHexString(TransactionEncoder.signMessage(transaction, _credentials))
            val response = (instance.ethSendRawTransaction(encoded).sendAsync().get())
            return response != null && !response.hasError()
        }
    }

    class Configuration {
        companion object {
            val gasPrice = BigInteger.valueOf(18)
            val gasLimit = BigInteger.valueOf(4712303)

            val httpConnectionString:String
                get() = "http://$ME_NETWORK_LOCATION:$ME_NETWORK_PORT"
            val webSocketConnectionString: String
                get() = "ws://$ME_NETWORK_LOCATION:$ME_NETWORK_PORT"

            internal lateinit var _directoryPrefix: String
            private val ME_NETWORK_PORT = "8545"
            val ME_NETWORK_LOCATION = "81.169.218.221"
            //internal val ME_NETWORK_LOCATION = "136.144.185.48"
            private val _wallet_location = "/wallet/%S/"
            internal fun walletLocation(network: String): String {
                return _directoryPrefix + String.format(_wallet_location, network)
            }
        }
    }
}
