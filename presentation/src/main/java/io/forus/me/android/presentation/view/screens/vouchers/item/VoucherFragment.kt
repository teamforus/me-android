package io.forus.me.android.presentation.view.screens.vouchers.item

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding2.view.RxView
import io.forus.me.android.data.executor.JobExecutor
import io.forus.me.android.domain.interactor.LoadVoucherUseCase
import io.forus.me.android.domain.interactor.SendEmailUseCase
import io.forus.me.android.domain.models.qr.QrCode
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.UIThread
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.mappers.*
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.vouchers.item.dialogs.SendVoucherSuccessDialog
import io.forus.me.android.presentation.view.screens.vouchers.item.transactions.TransactionsAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_voucher.*
import kotlinx.android.synthetic.main.toolbar_view.*
import java.text.SimpleDateFormat
import java.util.*

private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

class VoucherFragment : ToolbarLRFragment<VoucherModel, VoucherView,
        VoucherPresenter>(), VoucherView, OnMapReadyCallback {

    companion object {
        private const val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        private const val VOUCHER_EXTRA = "VOUCHER_EXTRA"
        private const val POSITION_EXTRA = "POSITION_EXTRA"

        val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())


        fun newInstance(voucher: Voucher, position: Int = -1): VoucherFragment = VoucherFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(VOUCHER_EXTRA, voucher)
            bundle.putString(VOUCHER_ADDRESS_EXTRA, voucher.address)
            bundle.putInt(POSITION_EXTRA, position)

            it.arguments = bundle
        }

        fun newInstance(address: String): VoucherFragment = VoucherFragment().also {
            val bundle = Bundle()
            bundle.putString(VOUCHER_ADDRESS_EXTRA, address)

            it.arguments = bundle
        }
    }

    private var voucher: Voucher? = null
    private lateinit var address: String
    private lateinit var adapter: TransactionsAdapter

    private var map: GoogleMap? = null
    private var organizationLatLng: LatLng? = null
    private val sendEmailDialog: AlertDialog.Builder by lazy(LazyThreadSafetyMode.NONE) {
        AlertDialog.Builder(activity!!)
                .setTitle(R.string.send_voucher_email_dialog_title)
                .setPositiveButton(R.string.send_voucher_email_dialog_positive_button) { _, _ ->
                    sendEmailDialogShows.onNext(true)
                }
                .setNegativeButton(R.string.send_voucher_email_dialog_cancel_button) { _: DialogInterface, _: Int -> sendEmailDialogShows.onNext(false) }
                .setCancelable(false)

    }

    override val toolbarTitle: String
        get() = getString(R.string.vouchers_item)

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override val showInfo: Boolean
        get() = true

    private val shortToken = PublishSubject.create<String>()
    override fun getShortToken(): Observable<String> = shortToken

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun sendEmail(): Observable<Unit> = RxView.clicks(btn_email).map { Unit }

    override fun showInfo(): Observable<Unit> = RxView.clicks(info_button).map { Unit }

    private val sendEmailDialogShows = PublishSubject.create<Boolean>()
    private val sentEmailDialogShown = PublishSubject.create<Unit>()

    override fun sendEmailDialogShows(): Observable<Boolean> = sendEmailDialogShows

    override fun sentEmailDialogShown(): Observable<Unit> = sentEmailDialogShown

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_voucher, container, false).also {

        voucher = arguments?.getParcelable(VOUCHER_EXTRA)
        address = arguments?.getString(VOUCHER_ADDRESS_EXTRA, "") ?: ""
        adapter = TransactionsAdapter()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val position = arguments?.getInt(POSITION_EXTRA, -1)
//
//        ViewCompat.setTransitionName(voucher_card, "card_transition_name_$position")
//        ViewCompat.setTransitionName(name, "name_transition_name_$position")
//        ViewCompat.setTransitionName(value, "value_transition_name_$position")

        val mapViewBundle: Bundle? = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)

        map_view.onCreate(mapViewBundle)
        map_view.getMapAsync(this)

        rv_transactions.layoutManager = LinearLayoutManager(context)
        rv_transactions.adapter = adapter


        info_button.setOnClickListener {

            shortToken.onNext("");


        }

        val qrEncoded = QrCode(QrCode.Type.VOUCHER, address).toJson()
        iv_qr_icon.setQRText(qrEncoded)
        iv_qr_icon.setOnClickListener {
            (activity as? DashboardActivity)?.showPopupQRFragment(qrEncoded, resources.getString(R.string.voucher_qr_code_subtitle_with_fund_name, voucher?.fundName))
        }

        toolbar.setNavigationOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount ?: 0 > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }

        shopkeeper_call.setOnClickListener {
            callToShopkeeper(shopkeeper_phone.text.toString())
        }

        shopkeeper_email.setOnClickListener {
            emailToShopkeeper(shopkeeper_email.text.toString())
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        map_view?.onSaveInstanceState(mapViewBundle)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
    }

    override fun onStart() {
        super.onStart()
        map_view?.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view?.onStop()
    }

    override fun onPause() {
        map_view?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        map_view?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun createPresenter(): VoucherPresenter {
        val currencyDataMapper = CurrencyDataMapper()
        return VoucherPresenter(
                LoadVoucherUseCase(Injection.instance.vouchersRepository, JobExecutor(), UIThread()),
                SendEmailUseCase(Injection.instance.vouchersRepository, JobExecutor(), UIThread()),
                VoucherDataMapper(currencyDataMapper, TransactionDataMapper(currencyDataMapper, OrganizationDataMapper()), ProductDataMapper()),
                address,
                voucher,
                Injection.instance.accountRepository
        )
    }

    override fun render(vs: LRViewState<VoucherModel>) {
        super.render(vs)

        name.text = vs.model.item?.name
        type.text = vs.model.item?.organizationName
        value.text = "${vs.model.item?.currency?.name} ${vs.model.item?.amount?.toDouble().format(2)}"

        vs.model.item?.let { voucher ->
            setToolbarTitle(resources.getString(if (voucher.isProduct) R.string.vouchers_item_product else R.string.vouchers_item))
            adapter.transactions = voucher.transactions
            tv_transactions_title.visibility =
                    if (voucher.isProduct || voucher.transactions.isEmpty()) View.GONE else View.VISIBLE

            tv_created.visibility =
                    if (voucher.isProduct) View.GONE else View.VISIBLE

            shopkeeper_card.visibility =
                    if (voucher.isProduct) View.VISIBLE else View.GONE

            if (voucher.transactions.isNotEmpty()) {
                tv_created.text = resources.getString(R.string.voucher_created,
                        dateFormat.format(voucher.createdAt))
            }

            if (voucher.isProduct) {
                voucher.product?.organization?.let { organization ->
                    shopkeeper_title.text = organization.name
                    shopkeeper_address.text = organization.address
                    shopkeeper_email.text = organization.email

                    Glide.with(this)
                            .load(organization.logo)
                            .fitCenter()
                            .into(shopkeeper_logo)

                    shopkeeper_phone.text = organization.phone

                    val latLng = LatLng(organization?.lat ?: 0.0, organization?.lon ?: 0.0)
                    organizationLatLng = latLng
                    setMarker(latLng)
                }
            }
        }

        if (vs.model.shortToken != null) {

            val url: String = if (voucher?.fundWebShopUrl?.isNotEmpty()!! && vs.model.shortToken.isNotEmpty()) {
                voucher?.fundWebShopUrl + "auth-link?token=" + vs.model.shortToken + "&target=voucher-" + voucher?.address
            } else {
                "https://forus.io/"
            }


            openVoucherInfo(url)
        }




        when (vs.model.emailSend) {
            EmailSend.SEND -> showEmailSendDialog()
            EmailSend.SENT -> showEmailSentDialog()
            EmailSend.NOTHING -> Unit
        }
    }


    private fun openVoucherInfo(url: String) {
        /*val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.launchUrl(activity, Uri.parse(url))*/

        val i =  Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }





    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        val uiSettings = map?.uiSettings
        uiSettings?.isIndoorLevelPickerEnabled = false
        uiSettings?.isMyLocationButtonEnabled = false
        uiSettings?.isMapToolbarEnabled = false
        uiSettings?.isCompassEnabled = false
        uiSettings?.isZoomControlsEnabled = false
        uiSettings?.isScrollGesturesEnabled = false
        uiSettings?.isZoomGesturesEnabled = false

        organizationLatLng?.let {
            setMarker(it)
        }
    }

    fun blurBackground() {

        name.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        type.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        value.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        value.paint.maskFilter = BlurMaskFilter(value.textSize / 4, BlurMaskFilter.Blur.NORMAL)
        name.paint.maskFilter = BlurMaskFilter(name.textSize / 4, BlurMaskFilter.Blur.NORMAL)
        type.paint.maskFilter = BlurMaskFilter(type.textSize / 4, BlurMaskFilter.Blur.NORMAL)
    }

    fun unblurBackground() {

        name.setLayerType(View.LAYER_TYPE_NONE, null)
        type.setLayerType(View.LAYER_TYPE_NONE, null)
        value.setLayerType(View.LAYER_TYPE_NONE, null)

        value.paint.maskFilter = null
        name.paint.maskFilter = null
        type.paint.maskFilter = null
    }

    private fun showEmailSentDialog() {
        SendVoucherSuccessDialog(context!!) {
            sentEmailDialogShown.onNext(Unit)
        }.show()
    }

    private fun setMarker(address: LatLng) {
        val marker = MarkerOptions()
        marker.position(address)

        map?.addMarker(marker)
                ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.shop_location))

        map?.moveCamera(CameraUpdateFactory.newLatLng(address))
    }

    private fun emailToShopkeeper(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")

        startActivity(Intent.createChooser(intent, getString(R.string.send_email_title)))
    }

    private fun callToShopkeeper(phone: String) {
        val uri = "tel:" + phone.trim()
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }

    private fun showEmailSendDialog() {
        sendEmailDialog.show()
    }

}

