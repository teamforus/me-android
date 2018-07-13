package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.select_type_list_item.view.*

class RecordTypeVH(parent: ViewGroup, private val clickListener: ((RecordType) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.select_type_list_item)) {
    init {

    }

    fun render(item:  RecordType) = with(itemView) {

        debug.text = item.name

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