package io.forus.me.android.presentation.view.screens.vouchers.item

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
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
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.UIThread
import io.forus.me.android.presentation.databinding.FragmentVoucherBinding
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.mappers.*
import io.forus.me.android.presentation.models.vouchers.FundType
import io.forus.me.android.presentation.models.vouchers.Office
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.component.images.AutoLoadImageView
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.FullscreenDialog
import io.forus.me.android.presentation.view.screens.vouchers.item.offices_adapter.OfficesAdapter
import io.forus.me.android.presentation.view.screens.vouchers.item.transactions.TransactionsAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*

private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

class VoucherFragment : ToolbarLRFragment<VoucherModel, VoucherView,
        VoucherPresenter>(), VoucherView, OnMapReadyCallback, MViewModelProvider<VoucherViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[VoucherViewModel::class.java].apply { }
    }

    var deleteButton: View? = null
    var editButton: View? = null
    var profileButton: View? = null
    override var info_button: AutoLoadImageView? = null

    companion object {
        private const val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        private const val VOUCHER_EXTRA = "VOUCHER_EXTRA"
        private const val POSITION_EXTRA = "POSITION_EXTRA"

        val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())


        fun newInstance(voucher: Voucher, position: Int = -1): VoucherFragment =
            VoucherFragment().also {
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
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.send_voucher_email_dialog_title)
            .setPositiveButton(R.string.send_voucher_email_dialog_positive_button) { _, _ ->
                sendEmailDialogShows.onNext(true)
            }
            .setNegativeButton(R.string.send_voucher_email_dialog_cancel_button) { _: DialogInterface, _: Int ->
                sendEmailDialogShows.onNext(
                    false
                )
            }
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

    private var canShowInfo: Boolean = false
    private val shortToken = PublishSubject.create<String>()
    override fun getShortToken(): Observable<String> = shortToken

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    override fun sendEmail(): Observable<Unit> = RxView.clicks(binding.btnEmail).map { Unit }

    override fun showInfo(): Observable<Unit> = RxView.clicks(info_button!!).map { Unit }

    private val sendEmailDialogShows = PublishSubject.create<Boolean>()
    private val sentEmailDialogShown = PublishSubject.create<Unit>()

    override fun sendEmailDialogShows(): Observable<Boolean> = sendEmailDialogShows

    override fun sentEmailDialogShown(): Observable<Unit> = sentEmailDialogShown

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyPresenter", "onCreate()")
    }

    private lateinit var binding: FragmentVoucherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoucherBinding.inflate(inflater)

        Log.d("MyPresenter", "onCreateView()")
        // voucher = arguments?.getParcelable(VOUCHER_EXTRA)
        // address = arguments?.getString(VOUCHER_ADDRESS_EXTRA, "") ?: ""
        voucher = viewModel.voucher.value
        address = viewModel.address.value ?: ""


        adapter = TransactionsAdapter()

        deleteButton = binding.root.findViewById(R.id.delete_button)
        editButton = binding.root.findViewById(R.id.edit_button)
        profileButton = binding.root.findViewById(R.id.profile_button)
        info_button = binding.root.findViewById(R.id.info_button)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapViewBundle: Bundle? = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)

        binding.mapView.onCreate(mapViewBundle)
        binding.mapView.getMapAsync(this)

        binding.rvTransactions.layoutManager =
            LinearLayoutManager(context)
        binding.rvTransactions.adapter = adapter


        info_button?.setOnClickListener {

            canShowInfo = true
            shortToken.onNext("")


        }

        val qrEncoded = QrCode(QrCode.Type.VOUCHER, address).toJson()
        binding.ivQrIcon.setQRText(qrEncoded)
        binding.ivQrIcon.setOnClickListener {


            val fundName = voucher?.fundName
            val title = resources.getString(R.string.voucher_qr_code_description)

            var qrDescription = ""
            if (voucher?.expireDate?.isNotEmpty()!!) {

                qrDescription = if (voucher!!.expired) String.format(
                    resources.getString(R.string.voucher_qr_code_expired),
                    voucher?.expireDate
                ) else String.format(
                    resources.getString(R.string.voucher_qr_code_actual),
                    voucher?.expireDate
                )
            }


            (activity as? DashboardActivity)?.showPopupQRFragment(
                qrEncoded,
                fundName,
                title,
                qrDescription
            )


        }

        toolbar?.setNavigationOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount ?: 0 > 0) {
                activity?.supportFragmentManager?.popBackStack()

            } else {
                activity?.onBackPressed()
            }
        }

        binding.shopkeeperCall.setOnClickListener {
            callToShopkeeper(binding.shopkeeperPhone.text.toString())
        }

        binding.shopkeeperEmail.setOnClickListener {
            emailToShopkeeper(binding.shopkeeperEmail.text.toString())
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        binding.mapView.onSaveInstanceState(mapViewBundle)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun createPresenter(): VoucherPresenter {
        Log.d("MyPresenter", "createPresenter() ")
        val currencyDataMapper = CurrencyDataMapper()
        return VoucherPresenter(
            LoadVoucherUseCase(Injection.instance.vouchersRepository, JobExecutor(), UIThread()),
            SendEmailUseCase(Injection.instance.vouchersRepository, JobExecutor(), UIThread()),
            VoucherDataMapper(
                currencyDataMapper, TransactionDataMapper(
                    currencyDataMapper, OrganizationDataMapper(),

                    ProductDataMapper()
                ), ProductDataMapper(), OfficeDataMapper(SchedulerDataMapper())
            ),

            viewModel.address.value ?: "",
            viewModel.voucher.value,
            Injection.instance.accountRepository
        )
    }


    override fun render(vs: LRViewState<VoucherModel>) {
        super.render(vs)


        binding.name.text = vs.model.item?.name
        binding.type.text = vs.model.item?.organizationName




        vs.model.item?.let { voucher ->
            Log.d(
                "my",
                "render 01  voucher address= ${voucher.address}  voucherIdentyAddress = ${voucher.identyAddress}"
            )

            setToolbarTitle(resources.getString(if (voucher.isProduct) R.string.vouchers_item_product else R.string.vouchers_item))
            if (voucher.fundType == FundType.subsidies.name) {
                adapter.isActionsVoucher = true
            }
            adapter.transactions = voucher.transactions
            binding.tvTransactionsTitle.visibility =
                if (voucher.isProduct || voucher.transactions.isEmpty()) View.GONE else View.VISIBLE

            binding.tvCreated.visibility =
                if (voucher.isProduct) View.GONE else View.VISIBLE

            binding.shopkeeperCard.visibility =
                if (voucher.isProduct) View.VISIBLE else View.GONE

            if (voucher.transactions.isNotEmpty()) {
                binding.tvCreated.text = resources.getString(
                    R.string.voucher_created,
                    dateFormat.format(voucher.createdAt)
                )
            }

            if (voucher.isProduct) {
                voucher.product?.organization?.let { organization ->
                    binding.shopkeeperTitle.text = organization.name
                    binding.shopkeeperAddress.text = organization.address
                    binding.shopkeeperEmail.text = organization.email
                    binding.shopkeeperEmail.visibility = if ((organization.email
                            ?: "").isNotEmpty()
                    ) View.VISIBLE else View.GONE

                    Glide.with(requireContext())
                        .load(organization.logo)
                        .fitCenter()
                        .into(binding.shopkeeperLogo)

                    binding.shopkeeperPhone.text = organization.phone

                    val latLng = LatLng(organization.lat ?: 0.0, organization.lon ?: 0.0)
                    organizationLatLng = latLng
                    setMarker(latLng)
                }


                val officesList = voucher.offices
                val myOffices = mutableListOf<Office>()
                myOffices.addAll(officesList)
                if (myOffices.isNotEmpty()) {
                    val officesAdapter = OfficesAdapter(myOffices, requireContext())
                    officesAdapter.showMapCallback = object : OfficesAdapter.ShowMapCallback {
                        override fun showMap(office: Office) {
                            if (office.lat != null && office.lon != null) {
                                showGoogleMaps(office.lat!!, office.lon!!)
                            }
                        }
                    }
                    val officesCnt = myOffices.size
                    binding.branchesTV.text =
                        resources.getQuantityString(R.plurals.branches, officesCnt, officesCnt)

                    binding.viewPager.adapter = officesAdapter
                    binding.viewPager.setPadding(16, 20, 130, 20)
                    binding.viewPager.setOnPageChangeListener(object :
                        ViewPager.OnPageChangeListener {
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {
                            val office =
                                if (position >= 0 && position < myOffices.size) myOffices[position] else myOffices[0]
                            val latLng = LatLng(office.lat ?: 0.0, office.lon ?: 0.0)
                            organizationLatLng = latLng
                            setMarker(latLng)
                        }

                        override fun onPageSelected(position: Int) {}
                        override fun onPageScrollStateChanged(state: Int) {}
                    })


                } else {
                    binding.branchesTV.visibility = View.GONE
                }
            }




            if (voucher.expired || voucher.deactivated) {


                if (voucher.isProduct && voucher.isUsed || voucher.expired || voucher.deactivated) {

                    info_button?.visibility = View.INVISIBLE
                    binding.btnEmail.isEnabled = false
                    binding.btnEmail.visibility = View.GONE
                    binding.ivQrIcon.visibility = View.INVISIBLE
                    binding.tvVoucherExpired.visibility = View.VISIBLE

                    binding.value.visibility = View.GONE
                    binding.usedOrExpiredLb.visibility = View.VISIBLE

                    if (voucher.isProduct && voucher.isUsed) {

                        binding.usedOrExpiredLb.text =
                            binding.usedOrExpiredLb.context.getString(R.string.voucher_is_used)
                    } else if (voucher.expired) {


                        if (voucher.expireDate?.isNotEmpty()!!) {

                            binding.usedOrExpiredLb.text = if (voucher.expired) String.format(
                                resources.getString(R.string.voucher_qr_code_expired),
                                voucher?.expireDate
                            ) else String.format(
                                resources.getString(R.string.voucher_qr_code_actual),
                                voucher?.expireDate
                            )

                            binding.tvVoucherExpired.visibility = View.GONE

                        }


                        binding.tvVoucherExpired.text = if (voucher.expired) String.format(
                            resources.getString(R.string.voucher_qr_code_expired),
                            voucher.expireDate
                        ) else String.format(
                            resources.getString(R.string.voucher_qr_code_actual),
                            voucher.expireDate
                        )

                    } else if (voucher.deactivated) {
                        binding.tvVoucherExpired.visibility = View.INVISIBLE
                        binding.usedOrExpiredLb.text =
                            String.format(resources.getString(R.string.voucher_deactivated))
                    }
                } else {
                    binding.tvVoucherExpired.visibility = View.GONE
                    binding.value.visibility = View.VISIBLE
                    binding.usedOrExpiredLb.visibility = View.GONE
                    binding.value.text = "${vs.model.item?.currency?.name} ${
                        vs.model.item?.amount?.toDouble().format(2)
                    }"
                }
            } else {
                info_button?.visibility = View.VISIBLE
            }

            if (voucher.fundType == FundType.subsidies.name) {
                info_button?.visibility = View.INVISIBLE



                if (voucher.fundType == FundType.subsidies.name) {
                    info_button?.visibility = View.INVISIBLE

                    binding.ivQrIcon.visibility = View.VISIBLE

                    binding.tvVoucherExpired.visibility = View.VISIBLE
                    binding.tvVoucherExpired.visibility = View.GONE
                    binding.value.visibility = View.GONE

                }

            }


            if (vs.model.shortToken != null) {
                if (canShowInfo) {

                    val url: String =
                        if (voucher?.fundWebShopUrl?.isNotEmpty()!! && vs.model.shortToken.isNotEmpty()) {
                            voucher?.fundWebShopUrl + "auth-link?token=" + vs.model.shortToken + "&target=voucher-" + voucher?.address
                        } else {
                            "https://forus.io/"
                        }

                    openVoucherInfo(url)
                    canShowInfo = false
                }
            }

            when (vs.model.emailSend) {


                EmailSend.SEND -> {
                    showEmailSendDialog()
                }

                EmailSend.SENT -> {
                    FullscreenDialog.display(
                        fragmentManager,
                        requireContext().resources.getString(R.string.voucher_send_email_success),
                        requireContext().resources.getString(R.string.voucher_send_email_description),
                        requireContext().resources.getString(R.string.me_ok)
                    ) {
                        sentEmailDialogShown.onNext(Unit)
                    }
                }

                EmailSend.NOTHING -> Unit
            }
        }

        info_button?.visibility = View.VISIBLE
    }


    private fun showGoogleMaps(lat: Double, lon: Double) {
        val gmmIntentUri = Uri.parse("geo:${lat.toString()},${lon.toString()}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(mapIntent)
        }
    }


    private fun openVoucherInfo(url: String) {


        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
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

