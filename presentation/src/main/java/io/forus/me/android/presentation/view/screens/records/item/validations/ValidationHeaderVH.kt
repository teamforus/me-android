package io.forus.me.android.presentation.view.screens.records.item.validations

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_validator_list_header.view.*

class ValidationHeaderVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_validator_list_header)) {
    init {

    }

    fun render(item: ValidationViewModel) = with(itemView) {
        tv_header.text = item.sectionName
    }
}