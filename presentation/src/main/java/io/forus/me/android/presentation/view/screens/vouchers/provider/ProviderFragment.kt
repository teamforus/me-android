package io.forus.me.android.presentation.view.screens.vouchers.provider

import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherBaseErrorDialog
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.forus.me.android.presentation.view.screens.vouchers.provider.categories.CategoriesAdapter
import io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.ApplyDialog
import io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.ChargeDialog
import io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.organizations_dialog.OrganizationsListDialog
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_voucher_provider.*
import kotlinx.android.synthetic.main.view_organization.*
import java.math.BigDecimal
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.SuccessDialogActivity


class ProviderFragment : ToolbarLRFragment<ProviderModel, ProviderView, ProviderPresenter>(),
    ProviderView,
    MViewModelProvider<VoucherViewModel> {

    override val viewModel by lazy {
        ViewModelProvider(requireActivity())[VoucherViewModel::class.java].apply { }
    }

    companion object {
        private val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        val IS_DEMO_VOUCHER = "IS_DEMO_VOUCHER"

        fun newIntent(id: String, isDemoVoucher: Boolean? = false): ProviderFragment =
            ProviderFragment().also {
                val bundle = Bundle()
                bundle.putSerializable(VOUCHER_ADDRESS_EXTRA, id)
                if (isDemoVoucher != null) bundle.putSerializable(IS_DEMO_VOUCHER, isDemoVoucher)
                it.arguments = bundle
            }
    }

    private lateinit var address: String
    private var isDemoVoucher: Boolean? = false
    private lateinit var categoriesAdapter: CategoriesAdapter

    override val toolbarTitle: String
        get() = if (isDemoVoucher != null && isDemoVoucher!!) getString(R.string.test_transaction) else getString(
            R.string.vouchers_provider
        )

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    private val selectAmount = PublishSubject.create<BigDecimal>()
    override fun selectAmount(): Observable<BigDecimal> = selectAmount

    private val selectNote = PublishSubject.create<String>()
    override fun selectNote(): Observable<String> = selectNote

    private val selectOrganization = PublishSubject.create<Organization>()
    override fun selectOrganization(): Observable<Organization> = selectOrganization

    private val charge = PublishSubject.create<BigDecimal>()
    override fun charge(): Observable<BigDecimal> = charge

    private val demoCharge = PublishSubject.create<BigDecimal>()
    override fun demoCharge(): Observable<BigDecimal> = demoCharge

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_voucher_provider, container, false).also {

        address =
            if (arguments == null) "" else requireArguments().getString(VOUCHER_ADDRESS_EXTRA, "")

        isDemoVoucher =
            if (arguments == null) false else requireArguments().getBoolean(IS_DEMO_VOUCHER, false)

        categoriesAdapter = CategoriesAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_categories.layoutManager =
            LinearLayoutManager(context)
        rv_categories.adapter = categoriesAdapter

        amount.setTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!amount.validate()) selectAmount.onNext(BigDecimal.ZERO)
                try {
                    selectAmount.onNext(BigDecimal(s.toString()))
                } catch (e: Exception) {
                    selectAmount.onNext(BigDecimal.ZERO)
                }
            }
        })

        note.setTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectNote.onNext(s.toString())
            }
        })

    }


    override fun createPresenter() = ProviderPresenter(
        Injection.instance.vouchersRepository,
        Injection.instance.firestoreTokenManager,
        viewModel.address.value ?: "",
        viewModel.isDemoVoucher.value
    )

    private var retrofitExceptionMapper: RetrofitExceptionMapper =
        Injection.instance.retrofitExceptionMapper

    override fun render(vs: LRViewState<ProviderModel>) {
        super.render(vs)

        amount.visibility =
            if (vs.model.item?.voucher?.isProduct == true) View.GONE else View.VISIBLE

        tv_name.text = vs.model.item?.voucher?.name
        tv_organization.text = vs.model.item?.voucher?.organizationName
        iv_icon.setImageUrl(vs.model.item?.voucher?.logo)


        if (isDemoVoucher != null && isDemoVoucher!!) {
            tv_organization_name.text =
                requireContext().getString(R.string.check_email_open_mail_app)
        } else {
            if (vs.model.selectedOrganization != null) {
                tv_organization_name.text = vs.model.selectedOrganization.name
                if (vs.model.selectedOrganization.logo?.isBlank() != true)
                    iv_organization_icon.setImageUrl(vs.model.selectedOrganization.logo)
            }
        }

        if (vs.model.item != null) {
            //categoriesAdapter.items = vs.model.item.allowedProductCategories

            if (vs.model.item.voucher.isProduct) {
                tv_fund.text = vs.model.item.voucher.fundName
            }
        }

        btn_make.active = vs.model.buttonIsActive
        btn_make.isEnabled = vs.model.buttonIsActive
        btn_make.setOnClickListener {
            if (vs.model.item != null) {
                payDialog(
                    vs.model.item.voucher.isProduct,
                    vs.model.selectedAmount,
                    vs.model.item.voucher.amount
                        ?: 0f.toBigDecimal()
                )

            }
        }

        container.setOnClickListener {
            OrganizationsListDialog(requireContext(), vs.model.item?.allowedOrganizations!!) {
                selectOrganization.onNext(it)
            }.show()
        }


        if (!(vs.model.selectedAmount.compareTo(BigDecimal.ZERO) == 0) && !vs.model.amountIsValid) amount.setError(
            resources.getString(R.string.vouchers_amount_invalid)
        )

        if (vs.model.makeTransactionError != null) {
            val error: Throwable = vs.model.makeTransactionError

            var errorMessage = getString(R.string.app_error_text)

            if (error is RetrofitException) {

                if (error.responseCode == 422) {
                    val detailsApiError = retrofitExceptionMapper.mapToDetailsApiError(error)
                    if (detailsApiError != null && detailsApiError.errors != null) {
                        errorMessage = detailsApiError.errorStringWithoutKey
                    }
                }
                if (error.responseCode == 403) {
                    val baseError = retrofitExceptionMapper.mapToBaseApiError(error)
                    val message = baseError.message
                    if (baseError != null && baseError.message != null) {
                        errorMessage = baseError.message
                    }
                }
            }

            ScanVoucherBaseErrorDialog(
                errorMessage,
                requireContext(),
                object : OnDismissListener, () -> Unit {
                    override fun invoke() {
                        activity?.finish()
                    }

                    override fun onDismiss(p0: DialogInterface?) {
                    }
                }).show()


        }

        if (vs.closeScreen && vs.model.makeTransactionError == null) {
            showScuccessDialog()
        }


    }

    private fun payDialog(isProduct: Boolean, amount: BigDecimal, balance: BigDecimal) {
        if (isDemoVoucher != null && isDemoVoucher!!) {
            demoCharge.onNext(BigDecimal.ZERO)
        } else
            if (isProduct) {
                ApplyDialog(requireContext()) {
                    charge.onNext(BigDecimal.ZERO)
                }.show()
            } else {
                val chargeAmount = if (amount <= balance) amount else balance
                val extra = if (amount <= balance) BigDecimal.ZERO else amount.minus(balance)
                ChargeDialog(requireContext(), chargeAmount, extra) {
                    charge.onNext(chargeAmount)
                }.show()
            }
    }


    private fun showScuccessDialog() {

        val i = SuccessDialogActivity.getCallingIntent(
            requireContext(),
            requireContext().resources.getString(R.string.vouchers_apply_success),
            requireContext().resources.getString(R.string.vouchers_transaction_duration_of_payout),
            requireContext().resources.getString(R.string.me_ok)
        )
        activity?.finish()
        activity?.startActivity(i)

        /*FullscreenDialog.display(fragmentManager,requireContext().resources.getString(R.string.success),
                requireContext().resources.getString(R.string.vouchers_apply_success),
                requireContext().resources.getString(R.string.me_ok)) { activity?.finish() }*/

    }

}