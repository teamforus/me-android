package io.forus.me.android.presentation.view.screens.records.newrecord.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.databinding.ItemRecordSelectValidatorBinding
import io.forus.me.android.presentation.databinding.ItemWalletsBinding
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.RecordValidatorVH
import io.forus.me.android.presentation.view.screens.wallets.WalletsVH

class RecordValidatorAdapter(private val clickListener: ((SimpleValidator) -> Unit)?): RecyclerView.Adapter<RecordValidatorVH>() {

    var checkedStatus = BooleanArray(0)
    var items: List<SimpleValidator> = emptyList()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = old.size
                override fun getNewListSize() = field.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == field[newItemPosition]
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == field[newItemPosition]
            }).dispatchUpdatesTo(this)
            notifyDataSetChanged()

            if(field.size != checkedStatus.size) checkedStatus = BooleanArray(field.size)
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordValidatorVH {
        val binding = ItemRecordSelectValidatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordValidatorVH(binding)
    }

    override fun onBindViewHolder(holder: RecordValidatorVH, position: Int) {
        val item = items[position]
        holder.bind(item, checkedStatus[position]){ validator: SimpleValidator, position: Int ->
            checkedStatus[position] = !checkedStatus[position]
            clickListener?.invoke(validator)
        }
    }


    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}