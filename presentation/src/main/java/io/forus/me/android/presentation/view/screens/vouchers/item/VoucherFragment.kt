package io.forus.me.android.presentation.view.screens.vouchers.item

import android.content.Intent
import android.graphics.BlurMaskFilter
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.forus.me.android.data.entity.vouchers.response.Product
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.vouchers.item.dialogs.SendVoucherSuccessDialog
import io.forus.me.android.presentation.view.screens.vouchers.item.transactions.TransactionsAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_voucher.*
import kotlinx.android.synthetic.main.toolbar_view.*
import java.text.SimpleDateFormat
import java.util.*
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat


class VoucherFragment : ToolbarLRFragment<VoucherModel, VoucherView, VoucherPresenter>(), VoucherView{

    companion object {
        private const val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())

        fun newIntent(id: String): VoucherFragment = VoucherFragment().also {
            val bundle = Bundle()
            bundle.putString(VOUCHER_ADDRESS_EXTRA, id)
            it.arguments = bundle
        }
    }

    private var productId: Long? = null
    private lateinit var address: String
    private lateinit var adapter: TransactionsAdapter

    override val toolbarTitle: String
        get() = getString(R.string.vouchers_item)

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override val showInfo: Boolean
        get() = true

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun sendEmail(): Observable<Unit> = RxView.clicks(btn_email).map { Unit }

    override fun showInfo(): Observable<Unit> = RxView.clicks(info_button).map { Unit }

    private val sendEmailDialogShown = PublishSubject.create<Unit>()
    override fun sendEmailDialogShown(): Observable<Unit> = sendEmailDialogShown

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_voucher, container, false).also {

        address = arguments?.getString(VOUCHER_ADDRESS_EXTRA, "") ?: ""
        adapter = TransactionsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_transactions.layoutManager = LinearLayoutManager(context)
        rv_transactions.adapter = adapter

        info_button.setOnClickListener {
            val url = when {
                productId != null ->
                    "https://" + BuildConfig.PREFIX_URL + "zuidhorn.forus.io/#!/products/$productId"
                else -> "https://www.zuidhorn.nl/kindpakket"

            }

            val intentBuilder = CustomTabsIntent.Builder()
            intentBuilder.setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            val customTabsIntent = intentBuilder.build()
            customTabsIntent.launchUrl(activity, Uri.parse(url))
        }

        val qrEncoded = QrCode(QrCode.Type.VOUCHER, address).toJson()
        iv_qr_icon.setQRText(qrEncoded)
        iv_qr_icon.setOnClickListener {
            (activity as? VoucherActivity)?.showPopupQRFragment(qrEncoded)
        }
    }


    override fun createPresenter() = VoucherPresenter(
            Injection.instance.vouchersRepository,
            address
    )


    override fun render(vs: LRViewState<VoucherModel>) {
        super.render(vs)

        name.text = vs.model.item?.name
        type.text = vs.model.item?.organizationName
        value.text = "${vs.model.item?.currency?.name} ${vs.model.item?.amount?.toDouble().format(2)}"

        if(vs.model.item != null){
            setToolbarTitle(resources.getString(if(vs.model.item.isProduct) R.string.vouchers_item_product else R.string.vouchers_item))

            productId = vs.model.item.productId
            adapter.transactions = vs.model.item.transactions
            tv_transactions_title.text = resources.getText(if(vs.model.item.transactions.isEmpty()) R.string.vouchers_transactions_empty else R.string.vouchers_transactions)
            tv_created.text = resources.getString(R.string.voucher_created, dateFormat.format(vs.model.item.createdAt))
        }

        if(vs.model.emailSend){
            showEmailSendDialog()
        }
    }

    fun blurBackground(){

        name.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        type.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        value.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        value.paint.maskFilter = BlurMaskFilter(value.textSize / 4, BlurMaskFilter.Blur.NORMAL)
        name.paint.maskFilter = BlurMaskFilter(name.textSize / 4, BlurMaskFilter.Blur.NORMAL)
        type.paint.maskFilter = BlurMaskFilter(type.textSize / 4, BlurMaskFilter.Blur.NORMAL)
    }

    fun unblurBackground(){

        name.setLayerType(View.LAYER_TYPE_NONE, null)
        type.setLayerType(View.LAYER_TYPE_NONE, null)
        value.setLayerType(View.LAYER_TYPE_NONE, null)

        value.paint.maskFilter = null
        name.paint.maskFilter = null
        type.paint.maskFilter = null
    }

    fun showEmailSendDialog(){
        SendVoucherSuccessDialog(context!!) {
            sendEmailDialogShown.onNext(Unit)
        }.show()
    }

}

