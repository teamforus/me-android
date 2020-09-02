package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment
//import io.forus.me.android.data.entity.vouchers.response.Voucher
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import android.util.Log
import android.view.View
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.vouchers.provider.ProviderPartialChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*


class ActionPaymentViewModel : ViewModel() , Observable{

    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository

    var voucherAddress: String? = null

    @Bindable
    var note = MutableLiveData<String>()

    private var product:ProductSerializable? = null

    val productName = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()
    val orgName = MutableLiveData<String>()

    val success = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable?>()

    init {

        productName.value = ""
        productPrice.value = ""
        orgName.value = ""
        note.value = ""

        success.value = false
        error.value = null
    }

    public fun setProduct(product: ProductSerializable){
        
        this.product = product
        refreshUI()
    }

    fun onSaveClick(view: View?) {
        println("MainActivity.onSaveClick")
        Log.d("forus","onSaveClick")
        makeTransaction()
    }

    private fun refreshUI()
    {
        productPrice.postValue(NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                .format(product!!.price))

        productName.postValue(product!!.name)

        orgName.postValue(product!!.companyName)

        /* val url = item.photoURL
         if (url != null && url.isNotEmpty()) {
             Glide.with(context).load(url)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(icon)
         }*/

    }

    fun makeTransaction(){

        Log.d("forus","product!!.companyId="+product!!.companyId+" product!!.id="+product!!.id)

        vouchersRepository.makeActionTransaction(voucherAddress!!,  note.value ?: "",
                 product!!.id,product!!.companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                    Log.d("forus","12345ssSuccess_transaction")
                    success.postValue(true)

                }
                .onErrorReturn {
                    Log.d("forus","12345sserr"+it.toString() )
                }
                .subscribe()
    }


    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }



}
