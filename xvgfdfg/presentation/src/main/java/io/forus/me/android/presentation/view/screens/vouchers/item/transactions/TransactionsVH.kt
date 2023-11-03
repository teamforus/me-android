package io.forus.me.android.presentation.view.screens.vouchers.item.transactions

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ItemVoucherTranscationsListBinding
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment



class TransactionsVH(private val binding: ItemVoucherTranscationsListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var isActionsVoucher = false

    fun bind(
        item: Transaction,
        clickListener: ((Transaction) -> Unit)?
    ) {
        binding.apply {
            subtitle1.text = if (isActionsVoucher) {
                if (item.product != null) {
                    item.product!!.name
                } else {
                    ""
                }
            } else item.organization?.name
            overline1.text = if (isActionsVoucher) {
                if (item.product != null ) {
                    if(item.product!!.organization != null) {
                        item.product!!.organization!!.name
                    }else{ ""}
                } else { ""}
            } else VoucherFragment.dateFormat.format(item.createdAt)


            subtitle2.text = if (isActionsVoucher) "" else "-${item.amount.toFloat().format(2)}"

            overline2.text = if (isActionsVoucher) {
                VoucherFragment.dateFormat.format(item.createdAt)
            } else {
                if (item.type == Transaction.Type.Product)
                    binding.root.context.resources.getString(R.string.vouchers_item_product)
                else
                    binding.root.context.resources.getString(R.string.vouchers_transaction_payment)
            }

            if (item.organization?.logo?.isBlank() == false)
                ivLogo.setImageUrl(item.organization?.logo)

            root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }
}