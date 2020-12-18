package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup
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
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ProductSerializable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*


class PriceAgreementViewModel : ViewModel(), Observable {


    private var product: ProductSerializable? = null


    val paidOutBySponsorPrice  = MutableLiveData<String>()
    val paidOutBySponsorNameSubtitle  = MutableLiveData<String>()

    val totalPrice = MutableLiveData<String>()

    val discountByProviderName = MutableLiveData<String>()
    val discountByProviderPrice = MutableLiveData<String>()

    val contributionBySponsorName = MutableLiveData<String>()
    val contributionBySponsorPrice = MutableLiveData<String>()

    val userPrice = MutableLiveData<String>()

    init {

        paidOutBySponsorPrice.value = ""
        paidOutBySponsorNameSubtitle.value = ""

        discountByProviderName.value = ""
        discountByProviderPrice.value = ""

        contributionBySponsorName.value = ""
        contributionBySponsorPrice.value = ""

        userPrice.value = ""

    }

    public fun setProduct(product: ProductSerializable) {

        this.product = product
        refreshUI()
    }




    private fun refreshUI() {
       // productPrice.postValue(NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
         //       .format(product!!.price))

        //productName.postValue(product!!.name)



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
