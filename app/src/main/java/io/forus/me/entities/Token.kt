package io.forus.me.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.util.Log
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.IdentityService
import io.forus.me.services.Web3Service
import io.forus.me.web3.TokenContract
import java.text.NumberFormat

/**
 * Created by martijn.doornik on 16/02/2018.
 */
@Entity
open class Token(
        address: String = "",
        name: String = "",
        identity:String = IdentityService.currentAddress,
        var value: Long = 0,
        var decimals: Long = 18
) : WalletItem(address, name, identity) {
    override val amount: String
        get() = "%.${decimals}f".format(value.toFloat()*Math.pow(10.0, -1*decimals.toDouble()))

    val isEther: Boolean
            get() = this.address == ETHER_ADDRESS

    override fun sync(): Boolean {
        var changed = false
        try {
            if (!isEther) {
                val contract = TokenContract(this.address)
                val balance = contract.getBalance(Web3Service.account!!).send().value.toLong()
                var decimals = this.decimals
                try {
                    decimals = contract.getDecimals().send().value.toLong()
                } catch (e: Exception) {
                } // Contract does not have a decimals value: use default 18
                if (balance != this.value) {
                    changed = true
                    this.value = balance
                }
                if (decimals != this.decimals) {
                    changed = true
                    this.decimals = decimals
                }
            } else {
                val balanceBigInt = Web3Service.getEther()
                if (balanceBigInt != null) {
                    val balance = balanceBigInt.toLong()
                    if (balance != this.value) {
                        this.value = balance
                        changed = true
                    }
                } else {
                    throw Exception()
                }

            }
        } catch (e: Exception) {
            val message = e.message ?: "Error with no message" // TODO fix hardcoding
            if (this.errorMessage != message) {
                changed = true
                this.errorMessage = message
            }
        }
        return changed
    }

    companion object {
        const val ETHER_ADDRESS = "ETHER"
    }
}