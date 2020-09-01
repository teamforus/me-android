package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.model
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.forus.me.android.domain.models.vouchers.ProductAction
//import io.forus.me.android.data.entity.vouchers.response.Voucher
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ActionsViewModel : ViewModel() {


    //var goodsPaginationSource: GoodsListDataSource? = null

    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository

    var productActionsLiveData: MutableLiveData<MutableList<ProductAction>> = MutableLiveData()


    /*val config: PagedList.Config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()*/

    /*var circleOptions: CircleOptions? = null
        get() = field
        set(value) {
            field = value
        }*/

    var voucherAddress: String? = null

    var voucher =  MutableLiveData<Voucher?>()

    val docNum = MutableLiveData<String>()

    val progress = MutableLiveData<Boolean>()
    val showWaitDialog = MutableLiveData<Boolean>()

    val isRefreshing = MutableLiveData<Boolean>()

    val showNetworkError = MutableLiveData<Boolean>()
    val showUnknownError = MutableLiveData<Boolean>()


    init {

        //circleOptions = CircleOptions(null,null)

        //transactionsLiveData.value

        //sortString.value = SortOrderTitle.title(SortOrder.DESC)

        voucher.value = null

        progress.value = true
        showWaitDialog.value = false
        isRefreshing.value = false
        showNetworkError.value = false

    }


    //fun getTransactions(): LiveData<List<Foo>> = transactionsLiveData

    public fun getVoucherDetails(){

        Log.d("forus","VOUSHER_ADDRESS="+voucherAddress)
        vouchersRepository.getVoucher("0xa27c3637b0b6ac12efe57df136d7774809d61c6f")//voucherAddress!!)//getVoucherAsProvider(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                    /*if(it != null) {
                        Log.d("forus", "We have voucher1 " + it!!.description+ " vouch "+it.address)
                    }else{
                        Log.d("forus", " voucher is null1")
                    }*/
                    voucher.postValue(it)




                }
                .onErrorReturn {

                }
                .subscribe()
    }

    public fun getVoucherActionGoods(page: Int){

        Log.d("forus","VOUSHER_ADDRESS="+voucherAddress)
        vouchersRepository.getVoucherProductsActionsAsProvider("0xa27c3637b0b6ac12efe57df136d7774809d61c6f")//voucherAddress!!)//getVoucherAsProvider(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                    Log.d("forus","ProductActionsSize= "+ it.size)

                    val arr: MutableList<ProductAction> = mutableListOf()
                    arr.addAll(it)
                    productActionsLiveData.postValue(arr)


                }
                .onErrorReturn {

                }
                .subscribe()
    }







}
