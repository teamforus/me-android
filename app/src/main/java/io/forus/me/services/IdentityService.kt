package io.forus.me.services

import android.arch.lifecycle.LiveData
import android.content.Context
import io.forus.me.entities.Identity
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.base.EthereumItemService
import java.util.concurrent.Callable

/**
 * Created by martijn.doornik on 23/02/2018.
 */
class IdentityService: EthereumItemService<Identity>() {

    override fun add(item: Identity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(item: Identity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun getItem(address: String): Identity {
        return DatabaseService.inject.identityDao().getIdentity(address)
    }

    override fun getList(identity: String): List<Identity> {
        return DatabaseService.inject.identityDao().getIdentities()
    }

    override fun getLiveData(identity: String): LiveData<List<Identity>> {
        return DatabaseService.inject.identityDao().getIdentityLiveData()
    }

    override fun getLiveItem(address: String): LiveData<Identity> {
        return DatabaseService.inject.identityDao().getLiveIdentity(address)
    }

    override fun getThread(): ThreadHelper.DataThread {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(item: Identity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val PREFERENCE_NAME: String = "currentIdentity"
        private val PREFERENCE_KEY: String = "id"

        var currentIdentity: Identity? = null//Identity("0x7b2afe6d5e16944084eaa292ecaa9c3b6469b445", "Mijn Overheid")
        val currentAddress:String
                get() {
                    if (currentIdentity != null) {
                        return currentIdentity!!.address
                    }
                    return ""
                }
        fun anyExists(withName: String? = null): Boolean {
            var count: Int? = 0
            count = if (withName != null) {
                DatabaseService.database?.identityDao()?.getIdentityCount(withName)
            } else {
                DatabaseService.database?.identityDao()?.getIdentityCount()
            }
             return  (count != null && count > 0)
        }

        fun getIdentityById(identityId: Long): Identity? {
            return DatabaseService.database?.identityDao()?.getIdentityById(identityId)
        }

        fun getIdentities(): LiveData<List<Identity>>? {
            return DatabaseService.database?.identityDao()?.getIdentityLiveData()
        }

        fun getIdentityList(): List<Identity> {
            val liveData = getIdentities()
            var list:List<Identity>? = emptyList()
            if (liveData != null) {
                list = ThreadHelper.await(Callable {
                    liveData.value
                })
            }
            if (list == null) list = emptyList()
            return list
        }

        fun loadCurrentIdentity(context: Context): Identity? {
            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val id = preferences.getLong(PREFERENCE_KEY, -1)
            if (id >= 0) {
                val identity = IdentityService.getIdentityById(id)
                this.currentIdentity = identity
            } else if (IdentityService.anyExists()) {
                val identities = IdentityService.getIdentityList()
                if (identities.isNotEmpty()) {
                    this.setCurrentIdentity(context, identities.first())
                }
            }
            return currentIdentity
        }

        fun newIdentity(address: String, name: String): Identity {
            val ret = Identity(address, name)
            val newId = ThreadHelper.await(Callable {
                DatabaseService.database?.identityDao()?.create(ret)
            })
            ret.id = newId
            return ret
        }

        fun setCurrentIdentity(context: Context, identity: Identity) {
            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val edit = preferences.edit()
            if (identity.id == null) {
                val identityId = DatabaseService.inject.identityDao().create(identity)
                edit.putLong(PREFERENCE_KEY, identityId)
            } else {
                edit.putLong(PREFERENCE_KEY, identity.id!!)
            }
            edit.apply()
        }
    }
}