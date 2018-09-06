package io.forus.me.android.presentation.view.screens.vouchers.provider.organizations

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.Organization
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_voucher_select_organization.view.*

class OrganizationVH(parent: ViewGroup, private val clickListener: ((Organization, Int) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_voucher_select_organization)) {
    init {

    }

    fun render(item: Organization, lastSelectedPosition: Int) = with(itemView) {
        rb_select_type.isChecked = lastSelectedPosition == adapterPosition
        tv_name.text = item.name
        root.setOnClickListener {
            clickListener?.invoke(item, adapterPosition)
        }

    }
}