package io.forus.me.android.presentation.view.screens.records.list

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.databinding.ItemRecordsBinding


class RecordsVH(private val binding: ItemRecordsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: Record,
        clickListener: ((Record) -> Unit)?
    ) {
        binding.apply {
            type.text = item.recordType.name
            value.text = item.value

            root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }
}