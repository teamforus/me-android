package io.forus.me.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.IdentityService

/**
 * Created by martijn.doornik on 16/02/2018.
 */
@Entity
class Voucher(
        address:String = "",
        name: String = "",
        identity:String = IdentityService.currentAddress,
        var value: Int = 0
) : WalletItem(address, name, identity) {
    override val amount: String
        get() = value.toString() + 'x'

    override fun sync(): Boolean {
        // todo
        return false
    }
}