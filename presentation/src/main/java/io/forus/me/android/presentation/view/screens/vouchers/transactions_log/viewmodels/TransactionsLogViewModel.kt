package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransactionsLogViewModel : ViewModel() {


    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository


    var transactionsLiveData: MutableLiveData<MutableList<Transaction>> = MutableLiveData()


    /*val config: PagedList.Config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()*/

    /*var circleOptions: CircleOptions? = null
        get() = field
        set(value) {
            field = value
        }*/

    val sortString = MutableLiveData<String>()

    val progress = MutableLiveData<Boolean>()
    val showWaitDialog = MutableLiveData<Boolean>()

    val isRefreshing = MutableLiveData<Boolean>()

    val showNetworkError = MutableLiveData<Boolean>()
    val showUnknownError = MutableLiveData<Boolean>()


    init {

        //circleOptions = CircleOptions(null,null)

        //transactionsLiveData.value

        //sortString.value = SortOrderTitle.title(SortOrder.DESC)

        progress.value = true
        showWaitDialog.value = false
        isRefreshing.value = false
        showNetworkError.value = false

    }


    val perPage: Int = 10

    public fun getTransactions(from: String, page: Int) {


        vouchersRepository.getTransactionsLogAsProvider(from, page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                    val arr: MutableList<Transaction> = mutableListOf()
                    arr.addAll(it)
                    transactionsLiveData.postValue(arr)
                    // init = true

                }
                .onErrorReturn {

                }
                .subscribe()
    }
}





