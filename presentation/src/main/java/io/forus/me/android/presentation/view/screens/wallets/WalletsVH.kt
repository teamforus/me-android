package io.forus.me.android.presentation.view.screens.wallets

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.wallets_item.view.*


class WalletsVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.wallets_item)) {
    init {

    }

    fun render(item:  Wallet) = with(itemView) {

        name.text = item.name
        currency_name.text = item.currency?.name
        value.text = item.balance.format()

        /// TODO change later
        //logo.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.etherium_icon))
        logo.setImageUrl(item.logoUrl)
    }
}