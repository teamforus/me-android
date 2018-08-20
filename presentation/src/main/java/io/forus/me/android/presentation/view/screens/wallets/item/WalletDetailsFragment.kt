package io.forus.me.android.presentation.view.screens.wallets.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import kotlinx.android.synthetic.main.wallet_details_fragment.*

class WalletDetailsFragment : ToolbarLRFragment<WalletDetailsModel, WalletDetailsView, WalletDetailsPresenter>(), WalletDetailsView{

    companion object {
        private val WALLET_ID_EXTRA = "WALLET_ID_EXTRA";

        fun newIntent(walletId: Long): WalletDetailsFragment = WalletDetailsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(WALLET_ID_EXTRA, walletId)
            it.arguments = bundle
        }
    }

    private var walletId: Long = 0

    override fun viewForSnackbar(): View = root

    override val toolbarTitle: String
        get() = getString(R.string.wallet_title_info)

    override fun loadRefreshPanel(): LoadRefreshPanel = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.wallet_details_fragment, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            walletId = bundle.getLong(WALLET_ID_EXTRA)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun createPresenter() = WalletDetailsPresenter(
            walletId,
            Injection.instance.walletsRepository
    )

    override fun render(vs: LRViewState<WalletDetailsModel>) {
        super.render(vs)

        val item = vs.model.item
        tv_balance_crypto.text = if(item != null) ""+item.balance.format()+" "+item.currency?.name else ""
        tv_balance_fiat.text = "?"
        iv_logo.setImageUrl(item?.logoUrl)
    }
}