package io.forus.me.android.presentation.view.screens.vouchers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.forus.me.android.presentation.models.vouchers.Voucher

class VoucherViewModel: ViewModel() {

    val TAG = "VoucherViewModel"

    private var _address =  MutableLiveData<String>()
    val address get() = _address
    fun setAddress(address: String){
        _address.value = address
    }

    private var _voucher =  MutableLiveData<Voucher?>()
    val voucher get() = _voucher
    fun setVoucher(voucher: Voucher?){
        _voucher.value = voucher
    }

    private var _isDemoVoucher =  MutableLiveData<Boolean>(false)
    val isDemoVoucher get() = _isDemoVoucher
    fun setIsDemoVoucher(isDemoVoucher: Boolean){
        _isDemoVoucher.value = isDemoVoucher
    }
}