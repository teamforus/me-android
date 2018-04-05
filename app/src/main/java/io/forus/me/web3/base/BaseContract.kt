package io.forus.me.web3.base

import io.forus.me.services.Web3Service
import org.web3j.tx.Contract

/**
 * Created by martijn.doornik on 04/04/2018.
 */
open class BaseContract : Contract {
    constructor(contractAddress: String) : this(contractAddress, Web3Service.getBytecodeOf(contractAddress))
    constructor(contractAddress: String, byteCode: String?) : super(byteCode, contractAddress, Web3Service.instance, Web3Service.credentials, Web3Service.Configuration.gasPrice, Web3Service.Configuration.gasLimit)
}
