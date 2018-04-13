package io.forus.me.web3

import android.util.Log
import io.forus.me.entities.Token
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.Web3Service
import io.forus.me.web3.base.BaseContract
import io.forus.me.web3.base.UpdateEvent
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.DefaultBlockParameterNumber
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.tx.RawTransactionManager
import org.web3j.utils.Numeric
import rx.Observable
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by martijn.doornik on 04/04/2018.
 */
class TokenContract(address:String) : BaseContract(address) {
    private val BALANCE_OF = "balanceOf"
    private val TRANSFER = "transfer"
    private val TRANSFER_EVENT = "Transfer"

    fun getBalance(address: String): RemoteCall<Uint> {
        val function = Function(
                BALANCE_OF,
                listOf(Address(address)),
                listOf(TypeReference.create(Uint::class.java))
        )
        return super.executeRemoteCallSingleValueReturn(function)
    }

    fun getTransferObservable(token: Token): Observable<TransferEvent> {
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
        var blockStart:DefaultBlockParameter = DefaultBlockParameterName.EARLIEST
        if (token.lastSync != null) {
            blockStart = DefaultBlockParameterNumber(token.lastSync!!)
        }
        val filter = EthFilter(blockStart, DefaultBlockParameterName.LATEST, this.contractAddress)
        filter.addSingleTopic(EventEncoder.encode(event))
        return this.web3j.ethLogObservable(filter).map { log ->
            val values = extractEventParametersWithLog(event, log)
            val from = values.indexedValues[0].typeAsString
            val to = values.indexedValues[1].typeAsString
            TokenContract.TransferEvent(from, to, log.blockNumber.toLong())
        }
    }

    fun transfer(to:String, amount: Long) : Boolean {
        val function = Function(
                TRANSFER,
                listOf(Address(to), Uint(BigInteger.valueOf(amount))),
                listOf(TypeReference.create(Bool::class.java))
        )
        val encodedFunction = FunctionEncoder.encode(function)

        val transactionManager = RawTransactionManager(this.web3j, Web3Service.credentials)
        val sentTransaction = transactionManager.sendTransaction(
                this.gasPrice,
                this.gasLimit,
                to,
                encodedFunction,
                BigInteger.ZERO
        )
        val hash = sentTransaction.transactionHash
        return hash != null
    }

    class TransferEvent(val from: String, val to:String, blockNumber: Long) : UpdateEvent(blockNumber)
}