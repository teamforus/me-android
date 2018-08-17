package io.forus.me.android.presentation.view.screens.records.item

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.Validator

class ValidatorsAdapter() : RecyclerView.Adapter<ValidatorVH>() {


    var validators: List<Validator> = emptyList()
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

    var clickListener: ((Validator) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ValidatorVH(parent, clickListener)
    override fun onBindViewHolder(holder: ValidatorVH, position: Int) {

        holder.render(validators[position])
    }
    override fun getItemCount() = validators.size
    override fun getItemId(position: Int) = position.toLong()
}