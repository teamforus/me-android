package io.forus.me.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.util.Log
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.IdentityService
import io.forus.me.services.Web3Service
import io.forus.me.web3.TokenContract

/**
 * Created by martijn.doornik on 16/02/2018.
 */
@Entity
class Token(
        address: String = "",
        name: String = "",
        identity:String = IdentityService.currentAddress,
        var value: Float = 0.0F
) : WalletItem(address, name, identity) {
    override val amount: String
        get() = value.toString()

    override fun sync(): Boolean {
        // todo address of person = 0x5af35941619b85b0f0c2508a4ae62a9763bb8ff1
        try {
            val contract = TokenContract(address)
            val ethCall = contract.getBalance(Web3Service.credentials!!.address)
            val ethResult = ethCall.send()
            val ethValue = ethResult.value.toFloat()
            if (ethValue != this.value) {
                this.value = ethValue
                return true
            }
        } catch (e: Exception) {
            Log.e("Token", e.message)
        }
        return false
    }
}