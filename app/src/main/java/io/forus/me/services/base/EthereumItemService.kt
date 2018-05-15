package io.forus.me.services.base

import android.arch.lifecycle.LiveData
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.IdentityService

/**
 * Created by martijn.doornik on 01/05/2018.
 */
abstract class EthereumItemService<ITEM: EthereumItem>: BaseService {
    abstract fun add(item: ITEM)
    abstract fun delete(item: ITEM)
    abstract fun getItem(address: String): ITEM
    abstract fun getList(identity: String = IdentityService.currentAddress): List<ITEM>
    abstract fun getLiveData(identity: String = IdentityService.currentAddress): LiveData<List<ITEM>>
    abstract fun getLiveItem(address: String): LiveData<ITEM>
    abstract fun getThread(): ThreadHelper.DataThread
    abstract fun update(item: ITEM)

    protected fun execute(task:Runnable) {
        getThread().postTask(task)
    }
}