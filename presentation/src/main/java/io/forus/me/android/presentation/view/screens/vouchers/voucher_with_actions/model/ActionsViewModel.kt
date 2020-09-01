package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.model
//import io.forus.me.android.data.entity.vouchers.response.Voucher
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import android.widget.AdapterView
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.models.vouchers.VoucherProvider
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ActionsViewModel : ViewModel() {


    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository

    var productActionsLiveData: MutableLiveData<MutableList<ProductAction>> = MutableLiveData()


    var voucherAddress: String? = null

    var voucher = MutableLiveData<VoucherProvider?>()

    val actionName = MutableLiveData<String>()
    val fundName = MutableLiveData<String>()

    val clearItems = MutableLiveData<Boolean>()

    val progress = MutableLiveData<Boolean>()
    val showWaitDialog = MutableLiveData<Boolean>()

    val isRefreshing = MutableLiveData<Boolean>()

    val showNetworkError = MutableLiveData<Boolean>()
    val showUnknownError = MutableLiveData<Boolean>()

    val perPage: Int = 10

    var organizationId: Long? = null

    init {


        voucher.value = null

        progress.value = true
        showWaitDialog.value = false
        isRefreshing.value = false
        showNetworkError.value = false

        clearItems.value = false

        actionName.value = ""
        fundName.value = ""

    }

    var init = true

    //  android:onItemSelected="@{(parent,view,pos,id)->model.onSelectItem(parent,view,pos,id)}"
    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
       // Log.d("forus", "Clear_items orgName=" + orgName + " organizationId=" + organizationId)
        if(init){
            init = false
        }else {

            val tv = view as android.support.v7.widget.AppCompatTextView

            val orgName = tv.text.toString()
            organizationId = selectedOrgIdByName(orgName)
            Log.d("forus", "Clear_items orgName=" + orgName + " organizationId=" + organizationId)
            clearItems.postValue(true)
            //clearItems.postValue(false)
        }

    }
    
     fun selectedOrgIdByName(name: String) : Long?{
        if(voucher.value == null) return null
        for(org in voucher.value!!.allowedOrganizations){
            if(org.name == name){
                organizationId = org.id
                return org.id
            }
        }
        return null
    }

    public fun getVoucherDetails() {

        vouchersRepository.getVoucherAsProvider("0xa27c3637b0b6ac12efe57df136d7774809d61c6f")//voucherAddress!!)//getVoucherAsProvider(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                    actionName.postValue(it.voucher.name)
                    organizationId = it.allowedOrganizations[0].id
                    fundName.postValue(it.voucher.organizationName)

                    voucher.postValue(it)
                }
                .onErrorReturn {

                }
                .subscribe()
    }

    public fun getVoucherActionGoods( page: Int) {

        Log.d("forus", "VOUSHER_ADDRESS=" + voucherAddress+ " orgId = "+organizationId)
        if (organizationId != null) {
            vouchersRepository.getVoucherProductsActionsAsProvider("0xa27c3637b0b6ac12efe57df136d7774809d61c6f", organizationId!!, page, perPage)//voucherAddress!!)//getVoucherAsProvider(address)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {

                        Log.d("forus", "ProductActionsSize= " + it.size)

                        val arr: MutableList<ProductAction> = mutableListOf()
                        arr.addAll(it)
                        productActionsLiveData.postValue(arr)
                        init = true


                    }
                    .onErrorReturn {

                    }
                    .subscribe()
        }
    }


}
