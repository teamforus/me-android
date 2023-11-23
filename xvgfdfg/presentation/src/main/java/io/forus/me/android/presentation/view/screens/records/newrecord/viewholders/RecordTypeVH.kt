package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.databinding.ItemRecordSelectTypeBinding


class RecordTypeVH(private val binding: ItemRecordSelectTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: RecordType,
        lastSelectedPosition: Int,
        clickListener: ((RecordType, Int) -> Unit)?
    ) {
        binding.apply {
            selectedCheckBox.setChecked(lastSelectedPosition == adapterPosition)
            name.text = item.name
            root.setOnClickListener {
                clickListener?.invoke(item, adapterPosition)
            }

        }
    }
}