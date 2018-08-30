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
import android.content.Intent
import android.net.Uri
import io.forus.me.android.domain.models.qr.QrCode


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

    private var address = ""

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
            if (arguments == null) "" else arguments!!.getString(ID_EXTRA, "")
    )


    override fun render(vs: LRViewState<VoucherModel>) {
        super.render(vs)

        address = vs.model.item?.address ?: ""

        name.text = vs.model.item?.name
        type.text = vs.model.item?.getValidString()
        value.text = "${vs.model.item?.currency?.name} ${vs.model.item?.amount?.toDouble().format(2)}"
        btn_qr.setImageUrl("https://image.flaticon.com/icons/png/512/107/107072.png")

        if(vs.model.item?.transactions != null){
            transactions_card.setTransactions(vs.model.item.transactions)
        }
    }
}

