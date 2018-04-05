package io.forus.me.web3

import io.forus.me.web3.base.BaseContract
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.request.EthFilter
import rx.Observable

/**
 * Created by martijn.doornik on 04/04/2018.
 */
class TokenContract(address:String) : BaseContract(address) {
    private val BALANCE_OF = "balanceOf"
    private val TRANSFER_EVENT = "Tranfer"

    fun getBalance(address: String): RemoteCall<Uint> {
        val function = Function(
                BALANCE_OF,
                listOf(Address(address)),
                listOf(TypeReference.create(Uint::class.java))
        )
        return super.executeRemoteCallSingleValueReturn(function)
    }

    fun getTransferObservable(): Observable<TransferEvent> {
        val event = Event(
                TRANSFER_EVENT,
                listOf(
                        TypeReference.create(Address::class.java),
                        TypeReference.create(Address::class.java)
                ),
                listOf(
                        TypeReference.create(Uint::class.java)
                )
        )
        val filter = EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, this.contractAddress)
        filter.addSingleTopic(EventEncoder.encode(event))
        return this.web3j.ethLogObservable(filter).map { log ->
            val values = extractEventParametersWithLog(event, log)
            val from = values.indexedValues[0].typeAsString
            val to = values.indexedValues[1].typeAsString
            val amount = values.nonIndexedValues[0].typeAsString.toLong()
            TokenContract.TransferEvent(from, to, amount)
        }
    }

    class TransferEvent(val from: String, val to:String, val amount: Long)
}