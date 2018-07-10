package io.forus.me.android.presentation.view.screens.records.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.format
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.records_list_item.view.*


class RecordsVH(parent: ViewGroup, private val clickListener: ((Record) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.records_list_item)) {
    init {

    }

    fun render(item:  Record) = with(itemView) {

//        type.text = "Valid til ${item.validDays} days"
//        name.text = item.name
//        value.text = "${item.currency.name} ${item.value.format()}"
//
//        logo.setImageUrl(item.logo)
//        root.setOnClickListener {
//            clickListener?.invoke(item)
//        }

    }
}