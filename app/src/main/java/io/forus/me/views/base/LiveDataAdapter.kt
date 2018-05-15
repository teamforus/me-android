package io.forus.me.views.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import io.forus.me.entities.base.EthereumItem
import io.forus.me.services.base.EthereumItemService

abstract class LiveDataAdapter<T : EthereumItem, VH : RecyclerView.ViewHolder>
        (lifecycleOwner: LifecycleOwner, service: EthereumItemService<T>): RecyclerView.Adapter<VH>() {
    protected var items: List<T> = emptyList()
    override fun getItemCount(): Int {
        return items.size
    }

    init {
        service.getLiveData().observe(lifecycleOwner, Observer {
            newItems: List<T>? ->
            run {
                if (newItems != null) {
                    this.items = newItems
                    notifyDataSetChanged()
                }
            }
        })
    }
}