package io.forus.me.android.presentation.view.screens.records.item.validators

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup


class ValidatorsAdapter(private val clickListener: ((ValidatorViewModel) -> Unit)?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM_VALIDATOR = 1
    val TYPE_ITEM_P2P = 2

    var items: List<ValidatorViewModel> = emptyList()
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
        if (items[position].type == ValidatorViewModel.Type.header)
            return TYPE_HEADER

        if (items[position].type == ValidatorViewModel.Type.p2p)
            return TYPE_ITEM_P2P

        return TYPE_ITEM_VALIDATOR
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{

        return if (viewType == TYPE_HEADER) {
            ValidatorHeaderVH(parent)

        } else if(viewType == TYPE_ITEM_P2P){
            ValidatorP2PVH(parent)
        } else {
            ValidatorVH(parent) { item: ValidatorViewModel ->
                clickListener?.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ValidatorVH) {
            holder.render(items[position])
        }
        if (holder is ValidatorP2PVH) {
            holder.render(items[position])
        }
        else if(holder is ValidatorHeaderVH){
            holder.render(items[position])
        }
    }
    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}