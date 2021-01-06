package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.popup
//import io.forus.me.android.data.entity.vouchers.response.Voucher
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.NoPriceType
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.payment.ProductSerializable
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

    val priceAgreementVisiblity = MutableLiveData<Boolean>()
    val headPriceVisiblity = MutableLiveData<Boolean>()
    val totalPriceVisiblity = MutableLiveData<Boolean>()
    val discountProviderVisiblity = MutableLiveData<Boolean>()
    val contributionSponsorVisiblity = MutableLiveData<Boolean>()
    val totalAmountVisiblity = MutableLiveData<Boolean>()

    init {
        priceAgreementVisiblity.value = false
        headPriceVisiblity.value = false
        totalPriceVisiblity.value = false
        discountProviderVisiblity.value = false
        contributionSponsorVisiblity.value = false
        totalAmountVisiblity.value = false

        paidOutBySponsorPrice.value = ""
        paidOutBySponsorNameSubtitle.value = ""

        discountByProviderName.value = ""
        discountByProviderPrice.value = ""

        contributionBySponsorName.value = ""
        contributionBySponsorPrice.value = ""

        userPrice.value = ""

    }

    fun setProduct(product: ProductSerializable, fundName: String) {

        this.product = product
        this.fundName = fundName
        refreshUI()
    }


    private fun refreshUI() {

        val resources = getApplication<Application>().resources

        val nvtStr = resources.getString(R.string.price_agreement_n_v_t)


        headPriceVisiblity.value = !(product!!.isNoPrice &&
                product!!.noPriceType == NoPriceType.discount.name)


        totalPriceVisiblity.value = product!!.oldPrice != null

        priceAgreementVisiblity.value = true

        /*totalPrice.value = if (product!!.oldPrice == null) {
            nvtStr
        } else {
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(product!!.oldPrice.toDouble())
        }*/


        totalPrice.value = if (product!!.isNoPrice) {

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

        discountByProviderName.value = resources.getString(R.string.price_agreement_discount_by_provider_,
                product!!.companyName)

        discountByProviderPrice.value = if (product!!.oldPrice == null || product!!.price == null) {
            nvtStr
        } else {
            val providerDiscount = product!!.oldPrice!! - product!!.price
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(providerDiscount.toDouble())
        }


        contributionBySponsorPrice.value = if (product!!.price == null || product!!.priceUser == null) {
            nvtStr
        } else {
            val sponsorDiscount = product!!.price!! - product!!.priceUser
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(sponsorDiscount.toDouble())
        }

        contributionBySponsorName.value = resources.getString(R.string.price_agreement_contribution_by_sponsor_,
                fundName)

        val priceUserStr = if (product!!.priceUser == null) {
            nvtStr
        } else {
            NumberFormat.getCurrencyInstance(Locale("nl", "NL")).format(product!!.priceUser.toDouble())
        }




        userPrice.value = priceUserStr

        val priceValue: String = if (product!!.isNoPrice) {
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

        paidOutBySponsorPrice.value = priceValue//priceUserStr

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
