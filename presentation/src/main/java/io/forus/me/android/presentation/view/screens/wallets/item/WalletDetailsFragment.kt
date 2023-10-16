package io.forus.me.android.presentation.view.screens.wallets.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentWalletDetailsBinding
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
//import kotlinx.android.synthetic.main.fragment_wallet_details.*

class WalletDetailsFragment : ToolbarLRFragment<WalletDetailsModel, WalletDetailsView, WalletDetailsPresenter>(), WalletDetailsView{

    companion object {
        private val WALLET_ID_EXTRA = "WALLET_ID_EXTRA"

        fun newIntent(walletId: Long): WalletDetailsFragment = WalletDetailsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(WALLET_ID_EXTRA, walletId)
            it.arguments = bundle
        }
    }

    private var walletId: Long = 0

    override fun viewForSnackbar(): View = binding.root

    override val toolbarTitle: String
        get() = getString(R.string.wallet_title_info)

    override val allowBack: Boolean
        get() = true

    override fun loadRefreshPanel(): LoadRefreshPanel = binding.lrPanel
    
    private lateinit var binding: FragmentWalletDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentWalletDetailsBinding.inflate(inflater)
        
        val bundle = this.arguments
        if (bundle != null) {
            walletId = bundle.getLong(WALLET_ID_EXTRA)
        }
        return binding.root
    }

    override fun createPresenter() = WalletDetailsPresenter(
            walletId,
            Injection.instance.walletsRepository
    )

    override fun render(vs: LRViewState<WalletDetailsModel>) {
        super.render(vs)

        val item = vs.model.item
        binding.tvBalanceCrypto.text = if(item != null) ""+item.balance.format(5)+" "+item.currency?.name else ""
        binding.tvBalanceFiat.text = resources.getString(R.string.wallet_balance_fiat, (item?.balance?.times(244.60)).format(2), (item?.balance?.times(281.60)).format(2))
        binding.ivLogo.setImageUrl(item?.logoUrl)
    }
}