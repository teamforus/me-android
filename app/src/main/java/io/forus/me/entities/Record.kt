package io.forus.me.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.IdentityService
import io.forus.me.services.RecordService
import io.forus.me.services.Web3Service
import java.util.concurrent.Callable

/**
 * Created by martijn.doornik on 27/02/2018.
 */
@Entity
class Record(
        address: String = "key",
        name: String = "",
        var recordCategoryId: Int = RecordService.CategoryIdentifier.OTHER,
        identity:String = IdentityService.currentAddress,
        @Ignore var value: String = "") : EthereumItem(address, name, identity) {
    val key:String
        get() = this.address

    override fun sync() : Boolean {
        this.value = ThreadHelper.await(Callable {
            Web3Service.HelloWorld.message
        })
        return true
    }
}