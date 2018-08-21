package io.forus.me.android.presentation.view.screens.vouchers.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.vouchers_list_item.view.*


class VouchersVH(parent: ViewGroup, private val clickListener: ((Voucher) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.vouchers_list_item)) {
    init {

    }

    fun render(item:  Voucher) = with(itemView) {

        type.text = item.getValidString()
        name.text = item.name
        value.text = "${item.currency.name} ${item.value.format()}"

        logo.setImageUrl(item.logo)
        root.setOnClickListener {
            clickListener?.invoke(item)
        }

    }
}