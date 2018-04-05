package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.entities.Service
import io.forus.me.services.base.BaseService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
class ServiceService : BaseService() {


    companion object {
        fun addService(service: Service) {
            DatabaseService.inject.insert(service)
        }

        fun getServicesByIdentity(identity:String): LiveData<List<Service>>? {
            return DatabaseService.database?.serviceDao()?.getServices(identity)
        }

        fun getServiceByAddressByIdentity(address:String, identity:String): Service? {
            return DatabaseService.database?.serviceDao()?.getServiceByAddressByIdentity(address, identity)
        }
    }
}