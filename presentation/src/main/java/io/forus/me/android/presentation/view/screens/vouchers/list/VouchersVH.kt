package io.forus.me.android.presentation.view.screens.vouchers.list

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import io.forus.me.android.presentation.models.vouchers.FundType
import kotlinx.android.synthetic.main.item_vouchers_list.view.*
import kotlinx.android.synthetic.main.toolbar_view.*


class VouchersVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_vouchers_list)) {

    fun render(item: Voucher) = with(itemView) {

        if(item.fundType == FundType.subsidies.name) {
            name.text = item.name
            organization_name.text = item.organizationName
            value.text = ""
            used.visibility = View.INVISIBLE
            if (item.logo != null) {
                if (item.logo!!.isNotEmpty()) {
                    logo.setImageUrl(item.logo)
                }
            }
        }else{

            name.text = item.name
            organization_name.text = item.organizationName
            value.text = "${item.currency?.name} ${item.amount?.toDouble().format(2)}"
            used.visibility = if (item.isProduct && item.isUsed) View.VISIBLE else View.GONE

            logo.setImageUrl(item.logo)

        }

    }
}