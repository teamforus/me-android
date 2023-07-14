package io.forus.me.android.presentation.view.screens.records.item.validations

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup


class ValidationAdapter(private val clickListener: ((ValidationViewModel) -> Unit)?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM_VALIDATOR = 1


    var items: List<ValidationViewModel> = emptyList()
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
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].type == ValidationViewModel.Type.header)
            return TYPE_HEADER


        return TYPE_ITEM_VALIDATOR
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{

        return if (viewType == TYPE_HEADER) {
            ValidationHeaderVH(parent)

        }  else {
            ValidationVH(parent) { item: ValidationViewModel ->
                clickListener?.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ValidationVH) {
            holder.render(items[position])
        }

        else if(holder is ValidationHeaderVH){
            holder.render(items[position])
        }
    }
    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}