package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class TransactionsLogViewModel : ViewModel() {


    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository


    var transactionsLiveData: MutableLiveData<MutableList<Transaction>> = MutableLiveData()

    val dateFormatForDisplay = SimpleDateFormat("d MMM YYYY", Locale.getDefault())
    val dateFormatForApi = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())


    val progress = MutableLiveData<Boolean>()
    val showWaitDialog = MutableLiveData<Boolean>()

    val isRefreshing = MutableLiveData<Boolean>()

    val showNetworkError = MutableLiveData<Boolean>()
    val showUnknownError = MutableLiveData<Boolean>()


    val calendarFrom = MutableLiveData<Calendar>()
    val calendarStringForDisplay = MutableLiveData<String>()
    val calendarStringForApi = MutableLiveData<String>()



    init {

        progress.value = true
        showWaitDialog.value = false
        isRefreshing.value = false
        showNetworkError.value = false

        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)

        calendarFrom.value = cal
        calendarStringForDisplay.value = ""
        calendarStringForApi.value = dateFormatForApi.format(cal.time)

        setCalendar(cal)

    }

    fun setCalendar(cal: Calendar){

        calendarFrom.value = cal
        calendarStringForDisplay.value = (dateFormatForDisplay.format(calendarFrom.value!!.time))
        calendarStringForApi.value = (dateFormatForApi.format(calendarFrom.value!!.time))
    }


    val perPage: Int = 20

    fun getTransactions(page: Int) {
        progress.postValue(true)
        vouchersRepository.getTransactionsLogAsProvider(calendarStringForApi.value!!, page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    progress.postValue(false)
                    val arr: MutableList<Transaction> = mutableListOf()
                    arr.addAll(it)
                    transactionsLiveData.postValue(arr)

                }
                .onErrorReturn {
                    progress.postValue(false)
                }
                .subscribe()
    }
}





