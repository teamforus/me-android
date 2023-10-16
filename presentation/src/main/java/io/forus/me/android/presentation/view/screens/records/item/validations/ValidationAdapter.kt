package io.forus.me.android.presentation.view.screens.records.item.validations

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.databinding.ItemRecordSelectValidatorBinding
import io.forus.me.android.presentation.databinding.ItemValidationListBinding
import io.forus.me.android.presentation.databinding.ItemValidatorListHeaderBinding
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.RecordValidatorVH


class ValidationAdapter(private val clickListener: ((ValidationViewModel) -> Unit)?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM_VALIDATOR = 1


    var items: List<ValidationViewModel> = emptyList()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = old.size
                override fun getNewListSize() = field.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition] == field[newItemPosition]

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition] == field[newItemPosition]
            }).dispatchUpdatesTo(this)
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].type == ValidationViewModel.Type.header)
            return TYPE_HEADER


        return TYPE_ITEM_VALIDATOR
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_HEADER) {
            val binding = ItemValidatorListHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ValidationHeaderVH(binding)
        } else {
            val binding = ItemValidationListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ValidationVH(binding)
            }
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ValidationVH) {
            val item = items[position]
            holder.bind(item){ item: ValidationViewModel ->
                    clickListener?.invoke(item)
            }

        } else if (holder is ValidationHeaderVH) {
            val item = items[position]
            holder.bind(item)
        }
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}