package io.forus.me.android.presentation.view.screens.records.item.validators

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.validator_list_header.view.*
import kotlinx.android.synthetic.main.validator_list_item.view.*

class ValidatorHeaderVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.validator_list_header)) {
    init {

    }

    fun render(item: ValidatorViewModel) = with(itemView) {
        tv_header.text = item.sectionName
    }
}