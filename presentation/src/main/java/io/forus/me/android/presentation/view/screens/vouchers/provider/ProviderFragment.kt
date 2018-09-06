package io.forus.me.android.presentation.view.screens.vouchers.provider

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.vouchers.provider.categories.CategoriesAdapter
import io.forus.me.android.presentation.view.screens.vouchers.provider.organizations.OrganizationsAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_voucher_provider.*

class ProviderFragment : ToolbarLRFragment<ProviderModel, ProviderView, ProviderPresenter>(), ProviderView{

    companion object {
        private val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"

        fun newIntent(id: String): ProviderFragment = ProviderFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(VOUCHER_ADDRESS_EXTRA, id)
            it.arguments = bundle
        }
    }

    private lateinit var address: String
    private lateinit var organizationsAdapter: OrganizationsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override val toolbarTitle: String
        get() = getString(R.string.vouchers_item)

    override val allowBack: Boolean
        get() = true

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    private val selectAmount = PublishSubject.create<Float>()
    override fun selectAmount(): Observable<Float> = selectAmount

    private val submit = PublishSubject.create<Boolean>()
    override fun submit(): Observable<Boolean> = submit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_voucher_provider, container, false).also {

        address = if (arguments == null) "" else arguments!!.getString(VOUCHER_ADDRESS_EXTRA, "")

        organizationsAdapter = OrganizationsAdapter {
            showToastMessage(it.name)
        }
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_organizations.layoutManager = LinearLayoutManager(context)
        rv_organizations.adapter = organizationsAdapter

        rv_categories.layoutManager = LinearLayoutManager(context)
        rv_categories.adapter = categoriesAdapter

        amount.setTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try{
                    selectAmount.onNext(s.toString().toFloat())
                }
                catch(e: Exception){
                    selectAmount.onNext(Float.NaN)
                    amount.setError(resources.getString(R.string.me_validation_error_decimal))
                }
            }
        })

        btn_make.setOnClickListener {
            submit.onNext(true)
        }
    }


    override fun createPresenter() = ProviderPresenter(
            Injection.instance.vouchersRepository,
            address
    )


    override fun render(vs: LRViewState<ProviderModel>) {
        super.render(vs)

        name.text = vs.model.item?.voucher?.name
        type.text = vs.model.item?.voucher?.getValidString()
        value.text = "${vs.model.item?.voucher?.currency?.name} ${vs.model.item?.voucher?.amount?.toDouble().format(2)}"
        btn_qr.setImageUrl(vs.model.item?.voucher?.logo)

        if(vs.model.item != null) {
            organizationsAdapter.items = vs.model.item.allowedOrganizations
            categoriesAdapter.items = vs.model.item.allowedProductCategories
        }

        btn_make.active = vs.model.buttonIsActive

        if(vs.model.makeTransactionError != null) showToastMessage(resources.getString(R.string.vouchers_make_transaction_failure))

        if(vs.closeScreen) closeScreen()
    }

    private fun closeScreen() {
        activity?.finish()
    }
}