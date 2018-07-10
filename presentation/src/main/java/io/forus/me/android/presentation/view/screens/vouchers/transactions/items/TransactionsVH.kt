package io.forus.me.android.presentation.view.screens.transactions.transactions.items

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.vouchers_transcations_list_item.view.*


class TransactionsVH(parent: ViewGroup, private val clickListener: ((Transaction) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.vouchers_transcations_list_item)) {
    init {

    }

    fun render(item: Transaction) = with(itemView) {

        type.text = item.type.name

        value.text = item.value.format()
        title.text = item.title
        
        root.setOnClickListener {
            clickListener?.invoke(item)
        }

    }
}