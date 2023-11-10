package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.model
//import io.forus.me.android.data.entity.vouchers.response.Voucher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import android.view.View
import android.widget.AdapterView
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.models.vouchers.VoucherProvider
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
//import io.forus.me.android.presentation.firestore_logging.FirestoreTokenManager
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ActionsViewModel : ViewModel() {


    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository
  //  var firestoreTokenManager: FirestoreTokenManager = Injection.instance.firestoreTokenManager

    var productActionsLiveData: MutableLiveData<MutableList<ProductAction>> = MutableLiveData()
    var productsListIsEmpty = MutableLiveData<Boolean>()


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


        //foo bar

        voucher.value = null

        productsListIsEmpty.value = false

        progress.value = true
        showWaitDialog.value = false
        isRefreshing.value = false
        showNetworkError.value = false

        clearItems.value = false

        actionName.value = ""
        fundName.value = ""

    }

    var init = true


    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if(init){
            init = false
        }else {

            val tv = view as androidx.appcompat.widget.AppCompatTextView

            val orgName = tv.text.toString()
            organizationId = selectedOrgIdByName(orgName)
            clearItems.postValue(true)
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

        vouchersRepository.getVoucherAsProvider(voucherAddress?:"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                  //  firestoreTokenManager.writeGetVoucherAsProvider(
                  //      voucherAddress?:"null", true, null
                  //  )

                    actionName.postValue(it.voucher.name?:"")
                    organizationId = it.allowedOrganizations[0].id
                    fundName.postValue(it.voucher.organizationName?:"")

                    voucher.postValue(it)
                }
                .onErrorReturn {
                  //  firestoreTokenManager.writeGetVoucherAsProvider(
                   //     voucherAddress?:"null", false, it.localizedMessage
                  //  )
                }
                .subscribe()
    }

    public fun getVoucherActionGoods( page: Int) {
        if (organizationId != null) {
            vouchersRepository.getVoucherProductsActionsAsProvider(voucherAddress!!, organizationId!!, page, perPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        val arr: MutableList<ProductAction> = mutableListOf()
                        arr.addAll(it)
                        productActionsLiveData.postValue(arr)
                        init = true


                        if(productActionsLiveData.value != null) {
                            if (productActionsLiveData.value!!.isEmpty()) {
                                productsListIsEmpty.postValue(true)
                            } else {
                                productsListIsEmpty.postValue(false)

                            }
                        }else {
                            productsListIsEmpty.postValue(true)
                        }

                    }
                    .onErrorReturn {

                    }
                    .subscribe()
        }
    }


}
