package io.forus.me.android.presentation.view.screens.vouchers.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ItemVouchersListBinding
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.models.vouchers.FundType
import io.forus.me.android.presentation.models.vouchers.Voucher


class VouchersVH(private val binding: ItemVouchersListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: Voucher,
        clickListener: ((Voucher) -> Unit)?
    ) {
        binding.apply {

            if (item.fundType == FundType.subsidies.name) {
                name.text = item.name
                usedOrExpiredLb.visibility = View.INVISIBLE
                if (item.logo != null) {
                    if (item.logo!!.isNotEmpty()) {
                        logo.setImageUrl(item.logo)
                    }
                }
            } else {

                name.text = item.name
                organizationName.text = item.organizationName

                if (item.logo != null) {
                    if (item.logo!!.isNotEmpty()) {
                        logo.setImageUrl(item.logo)
                    }
                }

                if (item.isProduct && item.isUsed) {
                    usedOrExpiredLb.visibility = View.VISIBLE
                    usedOrExpiredLb.text = usedOrExpiredLb.context.getString(R.string.voucher_is_used)
                } else if (item.expired) {
                    usedOrExpiredLb.visibility = View.VISIBLE
                    usedOrExpiredLb.text = usedOrExpiredLb.context.getString(R.string.voucher_expired)
                } else if (item.deactivated) {
                    usedOrExpiredLb.visibility = View.VISIBLE
                    usedOrExpiredLb.text =
                        usedOrExpiredLb.context.getString(R.string.voucher_deactivated)
                } else {
                    usedOrExpiredLb.visibility = View.GONE
                    value.text = "${item.currency?.name} ${item.amount?.toDouble().format(2)}"
                }

            }

            value.visibility = if (item.isProduct) {
                View.GONE
            } else {
                View.VISIBLE
            }
            root.setOnClickListener {
                clickListener?.invoke(item)
            }

        }
    }
}