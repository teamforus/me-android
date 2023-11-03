package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ItemRecordSelectCategoryBinding


class RecordCategoryVH(private val binding: ItemRecordSelectCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecordCategory, lastSelectedPosition: Int, clickListener: ((RecordCategory, Int) -> Unit)?) {
        binding.apply {
            bg.setBackgroundResource(if (lastSelectedPosition == adapterPosition) R.color.rippleColor else R.color.alabaster)
            tvName.text = item.name
            if (item.logo.isNotEmpty()) ivLogo.setImageUrl(item.logo)
            root.setOnClickListener {
                clickListener?.invoke(item, adapterPosition)
            }
        }
    }
}