package io.forus.me.android.presentation.view.screens.vouchers.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import kotlinx.android.synthetic.main.vouchers_details.*

/**
 * Fragment Assign Delegates Screen.
 */
class VoucherFragment : ToolbarLRFragment<VoucherModel, VoucherView, VoucherPresenter>(), VoucherView{

    companion object {
        private val ID_EXTRA = "ID_EXTRA"

        fun newIntent(id: String): VoucherFragment = VoucherFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ID_EXTRA, id)
            it.arguments = bundle
        }
    }

    override val toolbarTitle: String
        get() = getString(R.string.voucher)

    override val allowBack: Boolean
        get() = true

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.vouchers_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun createPresenter() = VoucherPresenter(
            Injection.instance.vouchersRepository,
            if (arguments == null) "" else arguments!!.getString(ID_EXTRA, "")
    )


    override fun render(vs: LRViewState<VoucherModel>) {
        super.render(vs)

        name.text = vs.model.item?.name
        type.text = vs.model.item?.getValidString()
        value.text = "${vs.model.item?.currency?.name} ${vs.model.item?.value.format()}"
        logo.setImageUrl("https://image.flaticon.com/icons/png/512/107/107072.png")



        transactions_card.setTransactions(vs.model.transactions)
    }


}

