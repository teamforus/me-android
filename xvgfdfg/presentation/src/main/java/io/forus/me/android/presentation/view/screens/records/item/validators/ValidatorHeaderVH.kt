package io.forus.me.android.presentation.view.screens.records.item.validators

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate

class ValidatorHeaderVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_validator_list_header)) {

    var tv_header: io.forus.me.android.presentation.view.component.text.TextView? = null

    init {
        tv_header = itemView.findViewById(R.id.tv_name)
    }

    fun render(item: ValidatorViewModel) = with(itemView) {
        tv_header?.text = item.sectionName
    }
}