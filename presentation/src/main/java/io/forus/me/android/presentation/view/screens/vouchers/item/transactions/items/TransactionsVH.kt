package io.forus.me.android.presentation.view.screens.vouchers.item.transactions.items

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_voucher_transcations_list.view.*


class TransactionsVH(parent: ViewGroup, private val clickListener: ((Transaction) -> Unit)?)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.item_voucher_transcations_list)) {
    init {

    }

    fun render(item: Transaction) = with(itemView) {

        subtitle1.text = item.organization.name
        overline1.text = item.dateTime

        subtitle2.text = item.amount.toFloat().format(2)

        if(!item.organization.logo.isBlank())
            iv_logo.setImageUrl(item.organization.logo)
        
        root.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}