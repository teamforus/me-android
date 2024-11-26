package io.forus.me.android.presentation.view.screens.vouchers.provider

import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentVoucherProviderBinding
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.component.editors.AmountTextInputEditText
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.qr.dialogs.ScanVoucherBaseErrorDialog
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.ConfirmExtraPaymentDialog
import io.forus.me.android.presentation.view.screens.vouchers.dialogs.SuccessDialogActivity
import io.forus.me.android.presentation.view.screens.vouchers.provider.categories.CategoriesAdapter
import io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.ApplyDialog
import io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.ChargeDialog
import io.forus.me.android.presentation.view.screens.vouchers.provider.dialogs.organizations_dialog.OrganizationsListDialog
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
//import kotlinx.android.synthetic.main.fragment_voucher_provider.*
//import kotlinx.android.synthetic.main.view_organization.*
import java.math.BigDecimal


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

    private var tv_organization_name: io.forus.me.android.presentation.view.component.text.TextView? = null
    private var iv_organization_icon: io.forus.me.android.presentation.view.component.images.AutoLoadImageView? = null
    private var containerOrg: View? = null

    override val toolbarTitle: String
        get() = if (isDemoVoucher != null && isDemoVoucher!!) getString(R.string.test_transaction) else getString(
            R.string.vouchers_provider
        )

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private val selectAmount = PublishSubject.create<BigDecimal>()
    override fun selectAmount(): Observable<BigDecimal> = selectAmount

    private val selectNote = PublishSubject.create<String>()
    override fun selectNote(): Observable<String> = selectNote

    private val selectOrganization = PublishSubject.create<Organization>()
    override fun selectOrganization(): Observable<Organization> = selectOrganization

        //private val charge = PublishSubject.create<BigDecimal>()
    //override fun charge(): Observable<BigDecimal> = charge

    private val charge = PublishSubject.create<Pair<BigDecimal, BigDecimal>>()
    override fun charge(): Observable<Pair<BigDecimal, BigDecimal>> = charge

    private val demoCharge = PublishSubject.create<BigDecimal>()
    override fun demoCharge(): Observable<BigDecimal> = demoCharge

    private lateinit var binding: FragmentVoucherProviderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentVoucherProviderBinding.inflate(inflater)

        address =
            if (arguments == null) "" else requireArguments().getString(VOUCHER_ADDRESS_EXTRA, "")

        isDemoVoucher =
            if (arguments == null) false else requireArguments().getBoolean(IS_DEMO_VOUCHER, false)

        categoriesAdapter = CategoriesAdapter()

        tv_organization_name = binding.root.findViewById(R.id.tv_organization_name)
        iv_organization_icon = binding.root.findViewById(R.id.iv_organization_icon)
        containerOrg = binding.root.findViewById(R.id.container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCategories.layoutManager =
            LinearLayoutManager(context)
        binding.rvCategories.adapter = categoriesAdapter


        binding.amountLn.setErrorTextColor(ColorStateList.valueOf(resources.getColor(R.color.error)))

        binding.amountInputText.onInputListener = object : AmountTextInputEditText.OnInputListener {
            override fun onInput(s: String) {
                val s1 = s.replace(",", ".")
                try {
                    selectAmount.onNext(BigDecimal(s1))
                } catch (e: NumberFormatException) {
                    selectAmount.onNext(BigDecimal.ZERO)
                }
            }

        }


        binding.note.setTextChangedListener(object : TextWatcher {
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


        binding.amountLn.visibility =
            if (vs.model.item?.voucher?.isProduct == true) View.GONE else View.VISIBLE

        binding.tvName.text = vs.model.item?.voucher?.name
        binding.tvOrganization.text = vs.model.item?.voucher?.organizationName
        binding.ivIcon.setImageUrl(vs.model.item?.voucher?.logo)

        binding.tvPrice.visibility = if (vs.model.item?.voucher?.amount_visible == true) View.VISIBLE else View.GONE
        vs.model.item?.voucher?.amount?.let{ amount ->
            binding.tvPrice.text = "€ ${amount.toFloat().format(2)}"
        }



        if (isDemoVoucher != null && isDemoVoucher!!) {
            tv_organization_name?.text =
                requireContext().getString(R.string.check_email_open_mail_app)
        } else {
            if (vs.model.selectedOrganization != null) {
                tv_organization_name?.text = vs.model.selectedOrganization.name
                if (vs.model.selectedOrganization.logo?.isBlank() != true)
                    iv_organization_icon?.setImageUrl(vs.model.selectedOrganization.logo)
            }
        }

        if (vs.model.item != null) {
            //categoriesAdapter.items = vs.model.item.allowedProductCategories

            if (vs.model.item.voucher.isProduct) {
                binding.tvFund.text = vs.model.item.voucher.fundName
            }
        }

        binding.btnMake.active = vs.model.buttonIsActive
        binding.btnMake.isEnabled = vs.model.buttonIsActive
        binding.btnMake.setOnClickListener {
            if (vs.model.item != null) {
                payDialog(
                    vs.model.item.voucher.isProduct,
                    vs.model.selectedAmount,
                    vs.model.item.voucher.amount
                        ?: 0f.toBigDecimal()
                )

            }
        }
        containerOrg?.setOnClickListener {
            OrganizationsListDialog(requireContext(), vs.model.item?.allowedOrganizations!!) {
                selectOrganization.onNext(it)
            }.show()
        }



        if (!(vs.model.selectedAmount.compareTo(BigDecimal.ZERO) == 0) && !vs.model.amountIsValid) binding.amountLn.setError(

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
                    charge.onNext(Pair(BigDecimal.ZERO, BigDecimal.ZERO))
                }.show()
            } else {

                val needExtra = amount > balance

                val chargeAmount = if (amount <= balance) amount else balance
                val extra = if (amount <= balance) BigDecimal.ZERO else amount.minus(balance)


                if(needExtra)
                {
                    val bottomSheetFragment = ConfirmExtraPaymentDialog(extraAmount = extra.toFloat(),
                        { extraAmount ->
                            charge.onNext(Pair(chargeAmount, extraAmount.toBigDecimal()))
                        }, {

                        })
                    bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")
                }
                else
                {
                    //charge.onNext(chargeAmount)
                    ChargeDialog(requireContext(), chargeAmount, 0.toBigDecimal() /*extra*/) {
                        charge.onNext(Pair(chargeAmount, 0.toBigDecimal()))
                    }.show()
                }

            }
    }


    private fun showScuccessDialog() {

        val i = SuccessDialogActivity.getCallingIntent(
            requireContext(),
            requireContext().resources.getString(R.string.vouchers_apply_success),
            requireContext().resources.getString(R.string.vouchers_transaction_duration_of_payout),
            requireContext().resources.getString(R.string.me_ok)
        )

        requireActivity().finish()
        requireActivity().startActivity(i)


    }

}