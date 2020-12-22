package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup
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
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment
import io.forus.me.android.presentation.view.screens.vouchers.provider.ProviderPartialChanges
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ProductSerializable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.NumberFormat
import java.util.*


class PriceAgreementViewModel(application: Application) : AndroidViewModel(application), Observable {


    private var product: ProductSerializable? = null
    var fundName: String? = null

    val paidOutBySponsorPrice = MutableLiveData<String>()
    val paidOutBySponsorNameSubtitle = MutableLiveData<String>()

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

    public fun setProduct(product: ProductSerializable, fundName: String) {

        this.product = product
        this.fundName = fundName
        refreshUI()
    }


    private fun refreshUI() {

        val resources = getApplication<Application>().resources

        val nvtStr = resources.getString(R.string.price_agreement_n_v_t)

        totalPrice.value = if (product!!.oldPrice == null) {
            nvtStr
        } else {
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(product!!.oldPrice)
        }

        discountByProviderName.value = resources.getString(R.string.price_agreement_discount_by_provider_,
                product!!.companyName)

        discountByProviderPrice.value = if (product!!.oldPrice == null || product!!.price == null) {
            nvtStr
        } else {
            val providerDiscount = product!!.oldPrice!! - product!!.price
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(providerDiscount)
        }


        contributionBySponsorPrice.value = if (product!!.price == null || product!!.priceUser == null) {
            nvtStr
        } else {
            val sponsorDiscount = product!!.price!! - product!!.priceUser
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(sponsorDiscount)
        }

        contributionBySponsorName.value = resources.getString(R.string.price_agreement_contribution_by_sponsor_,
                fundName)

        val priceUserStr = if (product!!.priceUser == null) {
            nvtStr
        } else {
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(product!!.priceUser)
        }

        userPrice.value = priceUserStr
        paidOutBySponsorPrice.value = priceUserStr

        paidOutBySponsorNameSubtitle.value = resources.getString(R.string.price_agreement_paid_by_customer)

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
