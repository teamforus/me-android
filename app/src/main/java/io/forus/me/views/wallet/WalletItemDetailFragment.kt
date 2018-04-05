package io.forus.me.views.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.forus.me.R
import io.forus.me.entities.base.EthereumItem
import io.forus.me.entities.base.WalletItem
import io.forus.me.views.base.TitledFragment

/**
 * Created by martijn.doornik on 30/03/2018.
 */
class WalletItemDetailFragment : TitledFragment() {

    var walletItem: EthereumItem? = null

    fun bindWalletItem(useView: View? = null, newWalletItem: WalletItem? = null) {
        if (newWalletItem != null) {
            walletItem = this.walletItem
        }
        var view = useView
        if (view == null) {
            view = this.view
        }
        if (walletItem == null || view == null) {
            // DO something
            throw Exception("View or wallet item not set!")
        }

        val qrView:ImageView = view.findViewById(R.id.qr_view)
        val bitmap = walletItem!!.qrCode
        qrView.setImageBitmap(bitmap)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_item_detail_fragment, container, false)
        if (walletItem != null) {
            bindWalletItem(view,null)
        }
        return view
    }

}