package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment
//import io.forus.me.android.data.entity.vouchers.response.Voucher
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import android.util.Log
import android.view.View
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.vouchers.provider.ProviderPartialChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*


class ActionPaymentViewModel(application: Application) : AndroidViewModel(application), Observable {

    var vouchersRepository: VouchersRepository = Injection.instance.vouchersRepository

    var voucherAddress: String? = null

    @Bindable
    var note = MutableLiveData<String>()

    private var product: ProductSerializable? = null

    val productName = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()
    val orgName = MutableLiveData<String>()

    val confirmPayment = MutableLiveData<Boolean>()
    val showPriceAgreement = MutableLiveData<Boolean>()
    val successPayment = MutableLiveData<Boolean>()
    val errorPayment = MutableLiveData<Throwable?>()

    val progress = MutableLiveData<Boolean>()

    init {

        productName.value = ""
        productPrice.value = ""
        orgName.value = ""
        note.value = ""
        progress.value = false

        confirmPayment.value = false
        showPriceAgreement.value = false
        successPayment.value = false
        errorPayment.value = null
    }

    public fun setProduct(product: ProductSerializable) {

        this.product = product
        refreshUI()
    }

    fun onSaveClick(view: View?) {
        confirmPayment.postValue(true)
    }

    fun onPricesClick(view: View?) {
        showPriceAgreement.postValue(true)
    }


    private fun refreshUI() {

        val resources = getApplication<Application>().resources


        val priceValue: String = if (product!!.noPrice) {
            if (product!!.noPriceType == NoPriceType.free.name) {
                resources.getString(R.string.free)
            } else {
                NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                        .format(product!!.priceUser)
            }
        } else {
            NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                    .format(product!!.priceUser)
        }

        productPrice.postValue(priceValue)

        productName.postValue(product!!.name)

        orgName.postValue(product!!.companyName)

        /* val url = item.photoURL
         if (url != null && url.isNotEmpty()) {
             Glide.with(context).load(url)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(icon)
         }*/

    }

    fun makeTransaction() {

        progress.postValue(true)
        vouchersRepository.makeActionTransaction(voucherAddress!!, note.value ?: "",  product!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    progress.postValue(false)
                    successPayment.postValue(true)
                }
                .onErrorReturn {
                    progress.postValue(false)
                    errorPayment.postValue(it)
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
