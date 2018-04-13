package io.forus.me.web3.base

import io.forus.me.services.Web3Service
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.text.DateFormat


/**
 * Created by martijn.doornik on 04/04/2018.
 */
open class BaseContract : Contract {
    constructor(contractAddress: String) : this(contractAddress, Web3Service.getBytecodeOf(contractAddress))
    constructor(contractAddress: String, byteCode: String?) : super(byteCode, contractAddress, Web3Service.instance, Web3Service.credentials, Web3Service.Configuration.gasPrice, Web3Service.Configuration.gasLimit)

    protected val nonce: BigInteger
            get() {
                val transactionCount = web3j.ethGetTransactionCount(this.contractAddress, DefaultBlockParameterName.PENDING)
                return Numeric.toBigInt(transactionCount.send().result)
                /*val unixTimestamp = System.currentTimeMillis()/1000L
                return BigInteger.valueOf(unixTimestamp)*/
            }
}
