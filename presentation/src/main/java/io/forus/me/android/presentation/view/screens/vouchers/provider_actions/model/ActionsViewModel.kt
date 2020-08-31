package io.forus.me.android.presentation.view.screens.vouchers.provider_actions.model
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.forus.me.android.presentation.view.screens.vouchers.provider_actions.Foo

class ActionsViewModel : ViewModel() {


    //var goodsPaginationSource: GoodsListDataSource? = null

    var transactionsLiveData: MutableLiveData<MutableList<Foo>> = MutableLiveData()


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


    //fun getTransactions(): LiveData<List<Foo>> = transactionsLiveData

    public fun fetchList() {

        val list : MutableList<Foo> = mutableListOf()
        list.add(Foo(1,"1"))
        list.add(Foo(1,"2"))
        list.add(Foo(1,"3"))

        //val liveFoo : MutableLiveData<List<Foo>> = MutableLiveData()
        //transactionsLiveData.value!!.clear()
        transactionsLiveData.postValue(list)//value!!.addAll(list)

    }


    fun refresh() {
        // goodsPaginationSource!!.invalidate()
    }




}
