package io.forus.me.android.presentation.view.screens.vouchers.item.transactions

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment
import kotlinx.android.synthetic.main.item_voucher_transcations_list.view.*
import java.text.SimpleDateFormat


class TransactionsVH(parent: ViewGroup)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.item_voucher_transcations_list)) {

    fun render(item: Transaction) = with(itemView) {

        subtitle1.text = item.organization.name
        overline1.text = VoucherFragment.dateFormat.format(item.createdAt)

        subtitle2.text = "-${item.amount.toFloat().format(2)}"
        overline2.text = if(item.type == Transaction.Type.Product) resources.getString(R.string.vouchers_item_product)
                            else resources.getString(R.string.vouchers_transaction_payment)

        if(!item.organization.logo.isBlank())
            iv_logo.setImageUrl(item.organization.logo)

    }
}