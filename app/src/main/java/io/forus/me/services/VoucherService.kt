package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.entities.Voucher
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.base.EthereumItemService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
class VoucherService : EthereumItemService<Voucher>() {

    override fun add(item: Voucher) {
        addVoucher(item)
    }

    override fun delete(item: Voucher) {
        deleteService(item)
    }

    override fun getItem(address: String): Voucher {
        return DatabaseService.inject.voucherDao().getVoucher(address)
    }

    override fun getList(identity: String): List<Voucher> {
        return DatabaseService.inject.voucherDao().getVouchers(identity)
    }

    override fun getLiveData(identity: String): LiveData<List<Voucher>> {
        return DatabaseService.inject.voucherDao().getVouchersLiveData(identity)
    }

    override fun getLiveItem(address: String): LiveData<Voucher> {
        return DatabaseService.inject.voucherDao().getLiveVoucher(address)
    }

    override fun getThread(): ThreadHelper.DataThread {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(item: Voucher) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val thread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.VOUCHER_THREAD)
        fun addVoucher(voucher: Voucher) {
            thread.postTask(Runnable { DatabaseService.inject.voucherDao().insert(voucher) })
        }

        fun deleteService(voucher: Voucher) {
            thread.postTask(Runnable { DatabaseService.inject.voucherDao().delete(voucher) })
        }

        fun getServicesByIdentity(identity:String): List<Voucher>? {
            return DatabaseService.database?.voucherDao()?.getVouchers(identity)
        }

        fun getServiceByAddressByIdentity(address:String, identity:String): Voucher? {
            return DatabaseService.database?.voucherDao()?.getVoucherByAddressByIdentity(address, identity)
        }

        fun updateService(voucher: Voucher) {
            thread.postTask(Runnable { DatabaseService.inject.voucherDao().update(voucher) })
        }
    }
}