package io.forus.me.android.presentation.view.screens.records.item.validators

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_validator_list_header.view.*

class ValidatorHeaderVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_validator_list_header)) {
    init {

    }

    fun render(item: ValidatorViewModel) = with(itemView) {
        tv_header.text = item.sectionName
    }
}