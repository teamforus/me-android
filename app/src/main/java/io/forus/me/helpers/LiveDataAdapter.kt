package io.forus.me.helpers

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.services.DatabaseService

/**
 * Created by martijn.doornik on 27/03/2018.
 */
abstract class LiveDataAdapter<T : EthereumItem, VH : RecyclerView.ViewHolder>
(activity: AppCompatActivity, data: LiveData<List<T>>): RecyclerView.Adapter<VH>() {
    protected var items: List<T> = emptyList()
    override fun getItemCount(): Int {
        return items.size
    }

    init {
        data.observe(activity, Observer {
            newItems: List<T>? ->
            run {
                if (newItems != null) {
                    val thread = ThreadHelper.dispense(ThreadHelper.WEB3_THREAD)
                    thread.postTask(Runnable {
                        for (item in items) {
                            // TODO detach listener
                        }

                        items = newItems
                        for (item in items) {
                            val changed = item.sync()
                            if (changed && item is Token) {
                                DatabaseService.inject.update(item)
                            }
                        }
                        activity.runOnUiThread({
                            this.notifyDataSetChanged()
                        })
                    })
                }
            }
        })
    }
}