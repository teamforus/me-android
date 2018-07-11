package io.forus.me.android.presentation.view.screens.vouchers.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.interfaces.FragmentListener
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.vouchers_item.*

/**
 * Fragment Assign Delegates Screen.
 */
class VoucherFragment : LRFragment<VoucherModel, VoucherView, VoucherPresenter>(), VoucherView, FragmentListener {

    companion object {
        private val ID_EXTRA = "ID_EXTRA"

        fun newIntent(id: String): VoucherFragment = VoucherFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ID_EXTRA, id)
            it.arguments = bundle
        }
    }


    override fun getTitle(): String = getString(R.string.valuta)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.vouchers_item, container, false)

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
        type.text = "Valid til ${vs.model.item?.validDays} days"
        value.text = "${vs.model.item?.currency?.name} ${vs.model.item?.value.format()}"
        logo.setImageUrl(vs.model.item?.logo)

        transactions_card.setTransactions(vs.model.transactions)
    }


}

