package io.forus.me.android.presentation.view.screens.wallets

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_wallets.view.*


class WalletsVH(parent: ViewGroup, private val clickListener: ((Wallet) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_wallets)) {
    init {

    }

    fun render(item:  Wallet) = with(itemView) {

        tv_name.text = item.name
        tv_currency_name.text = item.currency?.name
        tv_value.text = item.balance.format()
        iv_logo.setImageUrl(item.logoUrl)

        root.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}