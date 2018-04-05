package io.forus.me.entities

import android.arch.persistence.room.Entity
import io.forus.me.entities.base.EthereumItem
import io.forus.me.services.Web3Service

/**
 * Created by martijn.doornik on 23/02/2018.
 */
@Entity
class Identity(
        address:String = "",
        name: String = ""
) : EthereumItem(address, name, address) {
    override fun sync(): Boolean {
        // TODO
        return false
    }
}