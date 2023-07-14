package io.forus.me.android.presentation.view.screens.records.dialogs.validators_list_dialog

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R



class ValidatorsAdapter(var items: List<io.forus.me.android.domain.models.records.ValidatorOrganization>, val callback: Callback) : RecyclerView.Adapter<ValidatorsAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_validator, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iv_organization_icon = itemView.findViewById<io.forus.me.android.presentation.view.component.images.AutoLoadImageView>(R.id.iv_organization_icon)
        private val tv_organization_name = itemView.findViewById<io.forus.me.android.presentation.view.component.text.TextView>(R.id.tv_organization_name)
        fun bind(item: io.forus.me.android.domain.models.records.ValidatorOrganization) {
            tv_organization_name.text = item.name
           // if (item.logo?.isBlank() != true)
           //     iv_organization_icon.setImageUrl(item.logo)
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: io.forus.me.android.domain.models.records.ValidatorOrganization)
    }
}