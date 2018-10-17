package io.forus.me.android.presentation.view.screens.vouchers.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import kotlinx.android.synthetic.main.fragment_voucher.*
import android.content.Intent
import android.net.Uri
import io.forus.me.android.domain.models.qr.QrCode


class VoucherFragment : ToolbarLRFragment<VoucherModel, VoucherView, VoucherPresenter>(), VoucherView{

    companion object {
        private val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"

        fun newIntent(id: String): VoucherFragment = VoucherFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(VOUCHER_ADDRESS_EXTRA, id)
            it.arguments = bundle
        }
    }

    private lateinit var address: String

    override val toolbarTitle: String
        get() = getString(R.string.vouchers_item)

    override val allowBack: Boolean
        get() = true

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_voucher, container, false).also {

        address = if (arguments == null) "" else arguments!!.getString(VOUCHER_ADDRESS_EXTRA, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_info.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zuidhorn.nl/kindpakket"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        btn_qr.setOnClickListener {
            (activity as? VoucherActivity)?.showPopupQRFragment(QrCode(QrCode.Type.VOUCHER, address).toJson())
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

        if(vs.model.item?.transactions != null){
            transactions_card.setTransactions(vs.model.item.transactions)
        }
    }
}

