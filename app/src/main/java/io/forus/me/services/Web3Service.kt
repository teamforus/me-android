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
                    if (_credentials == null) return null
                    return _credentials
                }

        inline fun <reified Output: Type<Any>>call(address: String, function: String, input: MutableList<Type<Any>>): RemoteCall<Output> {
            val contract = Web3Contract(address)
            return contract.callSingleResult(function,input)
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
            return Configuration._directoryPrefix.isNotEmpty()
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

    class Web3Contract constructor(contractAddress: String) : Contract(Web3Service.getBytecodeOf(contractAddress), contractAddress, instance, credentials, Configuration.gasPrice, Configuration.gasLimit) {
        inline fun <reified T: Type<Any>>callSingleResult(
                functionName: String,
                input: MutableList<Type<Any>>
        ): RemoteCall<T> {
            val function = Function(
                    functionName,
                    input,
                    listOf(TypeReference.create(T::class.java))
            )
            return ThreadHelper.await(Callable {
                super.executeRemoteCallSingleValueReturn<T>(function)
            })
        }

        fun getMessage(): RemoteCall<Utf8String> {
            val function = Function(
                    "getMessage",
                    emptyList(),
                    listOf(TypeReference.create(Utf8String::class.java)))
            return ThreadHelper.await(Callable {
                super.executeRemoteCallSingleValueReturn<Utf8String>(function)
            })
        }

        fun setMessage(message:String): RemoteCall<TransactionReceipt> {
            val function = Function(
                    "setMessage",
                    listOf(Utf8String(message)),
                    emptyList())
            return executeRemoteCallTransaction(function)
        }

        val setMessageObservable: Observable<Utf8String>
            get() {
                val event = Event("MessageSet", emptyList(), listOf(TypeReference.create(Utf8String::class.java)))
                val filter = EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, this.contractAddress)
                filter.addSingleTopic(EventEncoder.encode(event))
                return this.web3j.ethLogObservable(filter).map<Utf8String> { log ->
                    val eventValues = this.extractEventParameters(event, log)
                    eventValues.nonIndexedValues[0] as Utf8String

                }

                /*{log: Log, ret: Utf8String ->
                    val eventValues = this.extractEventParameters(event, log)
                    return Utf8String(eventValues.nonIndexedValues[0].value.toString())
                }*/
            }
    }

    class HelloWorldContract constructor(contractAddress: String, web3j: Web3j, credentials: Credentials, gasPrice: BigInteger, gasLimit: BigInteger) : Contract(BYTECODE, contractAddress, web3j, credentials, gasPrice, gasLimit) {
        companion object {
            private val BYTECODE: String = "0x60606040526040805190810160405280600e81526020017f45657273746520626572696368740000000000000000000000000000000000008152506000908051906020019061004f929190610060565b50341561005b57600080fd5b610105565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a157805160ff19168380011785556100cf565b828001600101855582156100cf579182015b828111156100ce5782518255916020019190600101906100b3565b5b5090506100dc91906100e0565b5090565b61010291905b808211156100fe5760008160009055506001016100e6565b5090565b90565b6103bb806101146000396000f300606060405260043610610056576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680620267a41461005b578063368b877214610084578063ce6d41de146100e1575b600080fd5b341561006657600080fd5b61006e61016f565b6040518082815260200191505060405180910390f35b341561008f57600080fd5b6100df600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610178565b005b34156100ec57600080fd5b6100f461022e565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610134578082015181840152602081019050610119565b50505050905090810190601f1680156101615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6000602a905090565b806000908051906020019061018e9291906102d6565b507f3d7f415c35b881f2d0a109b3d9a1377e1e14afec5cc1fd06b563ed160c5e2630816040518080602001828103825283818151815260200191508051906020019080838360005b838110156101f15780820151818401526020810190506101d6565b50505050905090810190601f16801561021e5780820380516001836020036101000a031916815260200191505b509250505060405180910390a150565b610236610356565b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102cc5780601f106102a1576101008083540402835291602001916102cc565b820191906000526020600020905b8154815290600101906020018083116102af57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061031757805160ff1916838001178555610345565b82800160010185558215610345579182015b82811115610344578251825591602001919060010190610329565b5b509050610352919061036a565b5090565b602060405190810160405280600081525090565b61038c91905b80821115610388576000816000905550600101610370565b5090565b905600a165627a7a723058201e00874a2c9da71cf74c21950cbd8a71e0d617a4f57b2128ddcb55459129aede0029"
        }

        fun getMessage(): RemoteCall<Utf8String> {
            val function = Function(
                    "getMessage",
                    emptyList(),
                    listOf(TypeReference.create(Utf8String::class.java)))
            return super.executeRemoteCallSingleValueReturn<Utf8String>(function)
        }

        fun setMessage(message:String): RemoteCall<TransactionReceipt> {
            val function = Function(
                    "setMessage",
                    listOf(Utf8String(message)),
                    emptyList())
            return executeRemoteCallTransaction(function)
        }

        val setMessageObservable: Observable<Utf8String>
            get() {
                val event = Event("MessageSet", emptyList(), listOf(TypeReference.create(Utf8String::class.java)))
                val filter = EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, this.contractAddress)
                filter.addSingleTopic(EventEncoder.encode(event))
                return this.web3j.ethLogObservable(filter).map<Utf8String> { log ->
                    val eventValues = this.extractEventParameters(event, log)
                    eventValues.nonIndexedValues[0] as Utf8String

                }

                /*{log: Log, ret: Utf8String ->
                    val eventValues = this.extractEventParameters(event, log)
                    return Utf8String(eventValues.nonIndexedValues[0].value.toString())
                }*/
            }
    }

    class HelloWorld {

        interface EventListener {
            fun onMessageSet(message: String)
        }

        class EventWrapper(private val eventListener: EventListener) : Subscriber<Utf8String>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Log.e("Hello World", e!!.message)
            }

            override fun onNext(t: Utf8String?) {
                var message = "No message set?"
                if (t != null) {
                    message = t.value
                }
                eventListener.onMessageSet(message)
            }

        }

        companion object {
            private val address: String = "0xc74d2ad17ccbe51aeb18c8e9064ae107bc22103f"
            /*private val from: String = "0x7b2afe6d5e16944084eaa292ecaa9c3b6469b445"
            private val privateKey: String = "3897473832ebdbbd3f29f2c9b1e88a7d2fb40c0071dcbc073a3f17889bbac121"*/
            private val privateKey: String = "c95f188eb09b64ecc3293995668a98bbf9d071a073c5d0c6c5e192e7e217d154"


            private var listener: EventListener? = null

            private val GAS_PRICE = BigInteger.valueOf(18)
            private val MAX_GAS = BigInteger.valueOf(4712303)

            fun addListener(listener: EventListener) {
                this.listener = listener
                val dataThread = ThreadHelper.dispense(ThreadHelper.WEB3_THREAD)
                dataThread.start()
                val credentials: Credentials = Credentials.create(privateKey)
                val contract = HelloWorldContract(address, instance, credentials, GAS_PRICE, MAX_GAS)
                dataThread.postTask(Runnable {
                    contract.setMessageObservable.subscribe(EventWrapper(listener))
                })
                /*instance.ethLogObservable(filter).subscribe({ log: Log ->
                    //System.out.println(log.toString())
                                    } )*/

            }

            val message:String
                get() {
                    return try {
                        val contract = Web3Contract(address)
                        val result = contract.getMessage().send()
                        result.value
                    } catch (e: Exception) {
                        e.message!!
                    }
                }

            fun send(message:String): String {
                return try {
                    val credentials: Credentials = Credentials.create(privateKey)
                    val contract = HelloWorldContract(address, instance, credentials, GAS_PRICE, MAX_GAS)
                    val callable = Callable<TransactionReceipt>({contract.setMessage(message).send()})
                    val executor = Executors.newSingleThreadExecutor()
                    val future = executor.submit(callable)
                    future.get().gasUsed.toString()
                } catch (e: Exception) {
                    e.message!!
                }
            }
        }

    }
}
