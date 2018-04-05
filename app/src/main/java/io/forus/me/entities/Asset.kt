package io.forus.me.entities

import android.arch.persistence.room.Entity
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.IdentityService

/**
 * Created by martijn.doornik on 16/02/2018.
 */
@Entity
class Asset(
        address: String = "",
        name: String = "",
        identity:String = IdentityService.currentAddress
) : WalletItem(address, name, identity) {
    override val amount: String
        get() {
            return ""
        }

    override val label: String
            get() = this.name

    override fun sync(): Boolean {
        // Todo
        return false
    }
}