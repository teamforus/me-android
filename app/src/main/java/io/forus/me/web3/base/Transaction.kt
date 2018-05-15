package io.forus.me.web3.base

import org.web3j.protocol.core.methods.response.EthBlock

/**
 * Created by martijn.doornik on 09/04/2018.
 */
open class Transaction(val transactionHash: String, val blockNumber: Long)