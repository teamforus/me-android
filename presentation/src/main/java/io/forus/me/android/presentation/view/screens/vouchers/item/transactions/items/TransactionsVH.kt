package io.forus.me.android.presentation.view.screens.vouchers.item.transactions.items

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.vouchers_transcations_list_item.view.*


class TransactionsVH(parent: ViewGroup, private val clickListener: ((Transaction) -> Unit)?)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.vouchers_transcations_list_item)) {
    init {

    }

    fun render(item: Transaction) = with(itemView) {

        subtitle1.text = item.title
        overline1.text = item.type.name

        subtitle2.text = item.value.format()
        overline2.text = item.currency.name

        if(!item.currency.logoUrl.isEmpty()) logo.setImageUrl(item.currency.logoUrl)
        
        root.setOnClickListener {
            clickListener?.invoke(item)
        }
    }
}